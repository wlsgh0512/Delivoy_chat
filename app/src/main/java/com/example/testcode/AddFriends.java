package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityAddFriendsBinding;
import com.example.testcode.model.Add_Friends_Response;
import com.example.testcode.model.NonFriendsAdapter;
import com.example.testcode.model.NonFriendsData;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.User_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFriends extends AppCompatActivity {

    ActivityAddFriendsBinding binding;
    public ArrayList<NonFriendsData> all = new ArrayList<>();
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId,
            ucFriAreaNo, ucFriDistribId, ucFriAgencyId, ucFriMemCourId,
            selectUser, UserMemCourId, name;
    public List<FriendsResponse.Items.Rooms> items;

    NonFriendsAdapter Alladapter;

    ActionBar actionBar;

    public ArrayList<String> addedFriends = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvList2.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvList2.setLayoutManager(manager);
        Alladapter = new NonFriendsAdapter(all,this);
        binding.rvList2.setAdapter(Alladapter);

        actionBar = getSupportActionBar();  // 제목줄 객체 얻어오기
        actionBar.setTitle("친구추가");  // 액션바 제목설정
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");
        name = sharedPreferences.getString("name", "");

        fetchNonFriends();

    }   // end of OnCreate.

    public void addUserSetText(String string) {
        binding.addUser.setText(string);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_invite_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.create_chat:
                if (binding.addUser.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "추가할 친구를 선택하십시오.", Toast.LENGTH_SHORT).show();
                } else {
                    AddFriends();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchNonFriends() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.nonFriend(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId)
                    .enqueue(new retrofit2.Callback<FriendsResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<FriendsResponse> call,
                                               retrofit2.Response<FriendsResponse> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final FriendsResponse friendsResponse = response.body();

                                    items = friendsResponse.items.astRooms;
                                    for (int i = 0; i < items.size(); i++) {
                                        all.add(new NonFriendsData(items.get(i).acRealName, items.get(i).ucMemCourId));
                                    }
                                    all.remove(0);
                                    Alladapter.notifyDataSetChanged();


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
                        public void onFailure(retrofit2.Call<FriendsResponse> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
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

//                                    items = user_response.items.astUsers;
                                    for (int i = 0; i < items.size(); i++) {
                                        all.add(new NonFriendsData(items.get(i).acRealName, items.get(i).ucMemCourId));
                                    }
                                    Alladapter.notifyDataSetChanged();


                                    for(int i = all.size() -1 ; i >= 0; i--) {
                                        if (addedFriends.contains(all.get(i).getName())) {
                                            all.remove(i);
                                        }
                                    }
                                    Alladapter.notifyDataSetChanged();

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
            selectUser = sharedPreferences.getString("selectUser", "");
            ucFriMemCourId = sharedPreferences.getString("FriMemCour", "");

            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.addfriends(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId,
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
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

                                    Toast.makeText(getApplicationContext(),
                                            selectUser + "을 친구목록에 추가했습니다.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(AddFriends.this, ListActivity.class);
                                    startActivity(intent);
                                    Alladapter.notifyDataSetChanged();

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