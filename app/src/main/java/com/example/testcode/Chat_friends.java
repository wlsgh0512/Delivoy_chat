package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityChatFriendsBinding;
import com.example.testcode.databinding.ActivityJoinMemberBinding;
import com.example.testcode.model.Chat_Response;
import com.example.testcode.model.Code;
import com.example.testcode.model.DataItem;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.Join_Response;
import com.example.testcode.model.MyAdapter;
import com.example.testcode.model.PollingChat_Response;
import com.example.testcode.model.Send_msg_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 채팅창 화면.
 * response.body.string() 값 잘 받아오는것까지는 확인.
 * ArrayList에 add해서 채팅창에 갱신 확인.
 * 나오긴 하는데 화면에 진입했을 때 딱 안나오고 EditText를 한번 눌러야 나와서 (반응이 늦어서?)
 * 추후 작업 필요.
 * EditText를 눌렀을 때 마지막 채팅에 포커스가 맞춰지도록 .
 *
 * error code 500
 * talk_post = Expected BEGIN_OBJECT but was STRING at line 8 column 1 path $
 * 객체가 올것으로 기대 했는데 실제 데이터가 STRIN?
 */

public class Chat_friends extends AppCompatActivity {
    String msg = "";
    ArrayList<DataItem> dataList = new ArrayList<>();

    int getPosition;

    ActionBar actionBar;
//    ArrayList<DataItem> dataList = new ArrayList<DataItem>();

    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, name, getUiRoomNo;

    ActivityChatFriendsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
//        actionBar.setTitle("채팅");  //액션바 제목설정

        msg = binding.sendMsg.getText().toString();

//        initializeData();

//        get_talk();

        getPosition = getIntent().getIntExtra("uiRoomNo",1);
        getUiRoomNo = Integer.toString(getPosition);

        get_polling_talk();

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyvlerv.setLayoutManager(manager);
        binding.recyvlerv.setAdapter(new MyAdapter(dataList));  // Adapter 등록

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        SharedPreferences sharedPreferences= getSharedPreferences("test", MODE_PRIVATE);
        ucAreaNo = sharedPreferences.getString("ar","");
        ucDistribId = sharedPreferences.getString("di","");
        ucAgencyId = sharedPreferences.getString("ag","");
        ucMemCourId = sharedPreferences.getString("me","");
        name = sharedPreferences.getString("name","");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyvlerv.scrollToPosition(dataList.size() - 1);
            }
        }, 200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 대화상대 초대, 설정
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            /**
             * 대화상대 초대.
             * EditText 밑으로 친구 목록 리스트업.
             */
//            case R.id.invite:
//                Intent intent = new Intent(Chat_friends.this, Chat_invite_room.class);
//                startActivity(intent);
//                break;
//
//            case R.id.exit:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("채팅방을 나가시겠습니까?")
//                        .setCancelable(true)
//                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                return;
//                            }
//                        });
//                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        Intent intent1 = new Intent(Chat_friends.this, ListActivity.class);
//                        startActivity(intent1);
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // 전송 버튼을 눌렀을 때
    public void onClick_sendmsg(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyvlerv.scrollToPosition(dataList.size() - 1);
            }
        }, 200);
        send_msg();

    }

    /**
     * 파일 관련 버튼을 눌렀을 때
     * 다이얼로그 방식으로 띄우면 채팅을 방해하는 요소가 되니까
     * 다른 방법 찾을것.
     */
    public void addFile(View view) {

    }

    public void send_msg(){
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.send_msg("1",
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId,
                    binding.sendMsg.getText().toString())
                    .enqueue(new retrofit2.Callback<Send_msg_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Send_msg_Response> call,
                                               retrofit2.Response<Send_msg_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Send_msg_Response send_msg_response = response.body();

                                    msg = binding.sendMsg.getText().toString();
                                    dataList.add(new DataItem(msg, "", Code.ViewType.RIGHT_CONTENT));
                                    binding.sendMsg.setText("");

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
                        public void onFailure(retrofit2.Call<Send_msg_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void get_talk() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.chat("1",
                    getUiRoomNo,
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId)
                    .enqueue(new retrofit2.Callback<Chat_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Chat_Response> call,
                                               retrofit2.Response<Chat_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Chat_Response chat_response = response.body();


                                    for (int i = 0; i < chat_response.uiTalkNo.length(); i ++) {
                                        if (ucMemCourId != chat_response.ucMemCourId) {
                                            dataList.add(new DataItem(chat_response.acTalkMesg, chat_response.acRealName, Code.ViewType.LEFT_CONTENT));
                                            actionBar.setTitle(chat_response.acRealName);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
                                    Log.i("tag", error.message);
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<Chat_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void get_polling_talk() {
        try {

            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.pollingchat("1",
//                    "9",
                    getUiRoomNo,
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId)
                    .enqueue(new retrofit2.Callback<PollingChat_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<PollingChat_Response> call,
                                               retrofit2.Response<PollingChat_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final PollingChat_Response pollingChat_response = response.body();

                                    List<PollingChat_Response.Items.Rooms> items = pollingChat_response.items.astRooms;

                                    for(int i = 0 ; i < items.size(); i++) {
                                        if( !ucMemCourId.equals(pollingChat_response.items.astRooms.get(i).ucMemCourId)) {
                                            dataList.add(new DataItem(items.get(i).acTalkMesg, items.get(i).acRealName, Code.ViewType.LEFT_CONTENT));
                                        } else {
                                            dataList.add(new DataItem(items.get(i).acTalkMesg, name, Code.ViewType.RIGHT_CONTENT));
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
                                    Log.i("tag", error.message);
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<PollingChat_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void initializeData() {
        dataList = new ArrayList<>();
    }

}