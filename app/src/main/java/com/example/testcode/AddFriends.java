package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityAddFriendsBinding;
import com.example.testcode.databinding.FriendsListBinding;
import com.example.testcode.model.Add_Friends_Response;
import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.User_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFriends extends AppCompatActivity {

    ActivityAddFriendsBinding binding;
    ArrayList<String> all = new ArrayList<>();
    private ArrayAdapter Aadapter;
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId,
           ucFriAreaNo,ucFriDistribId,ucFriAgencyId,ucFriMemCourId;
    public List<User_Response.Items.Rooms> items;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Aadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, all) ;
        binding.allUser.setAdapter(Aadapter);

        actionBar = getSupportActionBar();  // 제목줄 객체 얻어오기
        actionBar.setTitle("친구추가");  // 액션바 제목설정
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");

        fetchAllUser();

        binding.allUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String bbb = items.get(i).acRealName;
                binding.addUser.setText(bbb);

                SharedPreferences sharedPreferences = getSharedPreferences(
                        "zzzz"
                        , MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("far", items.get(i).ucAreaNo);
                editor.putString("fd", items.get(i).ucDistribId);
                editor.putString("fag", items.get(i).ucAgencyId);
                editor.putString("fm", items.get(i).ucMemCourId);
                editor.commit();

                SharedPreferences spf = getSharedPreferences("pppp", MODE_PRIVATE);
                SharedPreferences.Editor edi = spf.edit();
                edi.putString("FriAreaNo", items.get(i).ucAreaNo);
                edi.putString("FriDistrib", items.get(i).ucDistribId);
                edi.putString("FriAgency", items.get(i).ucAgencyId);
                edi.putString("FriMemCour", items.get(i).ucMemCourId);
                edi.commit();

            }
        });


    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.create_chat:
                if(binding.addUser.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "추가할 친구를 선택하십시오.", Toast.LENGTH_SHORT).show();
                } else {
                    AddFriends();
                    Aadapter.notifyDataSetChanged();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_invite_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void fetchAllUser() {
        try {

            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.all_user(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId)
                    .enqueue(new retrofit2.Callback<User_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<User_Response> call,
                                               retrofit2.Response<User_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final User_Response user_response = response.body();

                                    items = user_response.items.astUsers;
                                    for (int i = 0; i < items.size(); i++) {
                                        all.add(items.get(i).acRealName);
                                    }
                                    Aadapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
//                                    Log.i("tag", error.message);
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<User_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddFriends() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("pppp", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            ucFriAreaNo = sharedPreferences.getString("FriAreaNo", "");
            ucFriDistribId = sharedPreferences.getString("FriDistrib", "");
            ucFriAgencyId = sharedPreferences.getString("FriAgency", "");
            ucFriMemCourId = sharedPreferences.getString("FriMemCour", "");

            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.addfriends(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId,
                    ucFriAreaNo,
                    ucFriDistribId,
                    ucFriAgencyId,
                    ucFriMemCourId)
                    .enqueue(new retrofit2.Callback<Add_Friends_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Add_Friends_Response> call,
                                               retrofit2.Response<Add_Friends_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Add_Friends_Response add_friends_response = response.body();

                                    Toast.makeText(getApplicationContext(), "친구 추가", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
//                                    Log.i("tag", error.message);
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<Add_Friends_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}