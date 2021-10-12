package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.content.DialogInterface;
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
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 채팅 메인 화면
 * 화면에 진입했을 때 '친구'탭이므로 친구목록이 바로 나타나게.
 */

public class ListActivity extends AppCompatActivity {
    String hostname = "222.239.254.253";
    ArrayList<String> item = new ArrayList<String>();
    ViewPager viewpager;
    TabLayout tabs;

    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("친구"));
        tabs.addTab(tabs.newTab().setText("채팅"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Toast.makeText(ListActivity.this, "Tab 1", Toast.LENGTH_SHORT).show();
                } else if (tab.getPosition() == 1) {
                    Toast.makeText(ListActivity.this, "Tab 2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Adapter
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapter myPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFragment(new Frag1());
        myPagerAdapter.addFragment(new Frag2());
        viewPager.setAdapter(myPagerAdapter);

        //탭 선택 이벤트
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("채팅");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");

    }

    // 우측 상단 표시하기위해
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // 우측 상단 새로운 채팅방 만들기, 설정
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.create_chat:
                Intent intent = new Intent(ListActivity.this, Create_chatroom.class);
                startActivity(intent);
                // 채팅방 만드는 화면 넣을것
                break;
            case R.id.shared_settings:
                Intent intent1 = new Intent(ListActivity.this, SettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.action_change_info:
                Intent intent2 = new Intent(ListActivity.this, Change_info.class);
                startActivity(intent2);
                break;
            case R.id.action_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("로그아웃 하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ListActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(ListActivity.this, "로그아웃하였습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Friends_list1() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.friend(ucAreaNo,
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
                                    item.add(friendsResponse.acRealName);
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
                        public void onFailure(retrofit2.Call<FriendsResponse> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
