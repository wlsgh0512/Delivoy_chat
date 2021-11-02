package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityListUpBinding;
import com.example.testcode.model.Chat_invite_room_Response;
import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.User_listup_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 채팅창 -> 채팅창에 참여중인 사용자 목록
 * 내비게이션 드로어를 통해 구현하려했으나 임시로 이렇게 구현..
 */

public class ListTest extends AppCompatActivity {

    ListView listtest;
    ArrayList<String> listlist = new ArrayList<>();
    private ArrayAdapter Fadapter;
    String testUiRoomNo, ucDistribId, ucAgencyId, ucAreaNo, ucMemCourId;
    List<User_listup_Response.Items.AstUser> items;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_up);

        /**
         * 조회에서 뒤로가기 했을 때 Chat_friends로
         * Chat_friends를 새로 진입하게 되면서 uiRoomNo 문제 ?
         */
//        actionBar = getSupportActionBar();  // 제목줄 객체 얻어오기
//        actionBar.setTitle("참여중인 사용자 조회");  // 액션바 제목설정
//        actionBar.setDisplayHomeAsUpEnabled(true);

        Fadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listlist) ;

        listtest = (ListView) findViewById(R.id.listtest) ;
        listtest.setAdapter(Fadapter) ;

        testUiRoomNo = getIntent().getStringExtra("RoomNo");
        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");

        list_Test();
        Fadapter.notifyDataSetChanged();


        listtest.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences spf = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor edi = spf.edit();
                edi.putInt("position", i);
                edi.commit();

                AlertDialog.Builder builder = new AlertDialog.Builder(ListTest.this);
                builder.setMessage("채팅방에서 삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        User_delete();
                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });


    }

    public void list_Test(){
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.list_up(
                    testUiRoomNo)
                    .enqueue(new retrofit2.Callback<User_listup_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<User_listup_Response> call,
                                               retrofit2.Response<User_listup_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final User_listup_Response user_listup_response = response.body();
                                    items = user_listup_response.items.astUsers;

                                    for (int i = 0; i < items.size(); i++) {
                                        listlist.add(items.get(i).acRealName);

                                        SharedPreferences spf = getPreferences(MODE_PRIVATE);
                                        SharedPreferences.Editor edi = spf.edit();
                                        edi.putString("memId", items.get(i).ucMemCourId);
                                        edi.commit();
                                    }
                                    Fadapter.notifyDataSetChanged();



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
                                    Log.i("tag", "" + error.message);
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<User_listup_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void User_delete(){
        try {
            SharedPreferences sharedPreferences1 = getPreferences(MODE_PRIVATE);
            int pos = sharedPreferences1.getInt("position", 0);

            SharedPreferences sharedPreferences2 = getPreferences(MODE_PRIVATE);
            ucMemCourId = sharedPreferences2.getString("memId", "");

            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.delete_user(
                    testUiRoomNo,
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId)
                    .enqueue(new retrofit2.Callback<Chat_invite_room_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Chat_invite_room_Response> call,
                                               retrofit2.Response<Chat_invite_room_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Chat_invite_room_Response chat_invite_room_response = response.body();

                                    listlist.remove(pos);
                                    Fadapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
                                    Log.i("tag", "" + error.message);
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<Chat_invite_room_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}