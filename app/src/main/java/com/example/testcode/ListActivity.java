package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.components.BackPressHandler;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.FriendsListBinding;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 채팅 메인 화면
 * 화면에 진입했을 때 '친구'탭이므로 친구목록이 바로 나타나게.
 */

public class ListActivity extends AppCompatActivity {

    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, pw;

    FriendsListBinding binding;

    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FriendsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tabs.addTab(binding.tabs.newTab().setText("친구"));
        binding.tabs.addTab(binding.tabs.newTab().setText("채팅"));
        binding.tabs.setTabGravity(binding.tabs.GRAVITY_FILL);

        //Adapter
        final ViewPagerAdapter myPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFragment(new Frag1());
        myPagerAdapter.addFragment(new Frag2());
        binding.viewpager.setAdapter(myPagerAdapter);

        //탭 선택 이벤트
        binding.tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.viewpager));
        binding.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabs));

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("채팅");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");
        pw = sharedPreferences.getString("pw","");

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
                break;
            case R.id.add_friends:
                Intent intent0 = new Intent(ListActivity.this, AddFriends.class);
                startActivity(intent0);
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

                SharedPreferences sharedPreferences = getSharedPreferences("auto", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed("뒤로가기 버튼을 한번 더 누르면 종료됩니다.", 3000);
    }

}
