package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.LoginResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 채팅 메인 화면
 * 화면에 진입했을 때 '친구'탭이므로 친구목록이 바로 나타나게.
 */

public class ListActivity extends AppCompatActivity {
    String hostname = "222.239.254.253";
    ArrayList<String> item = new ArrayList<String>();
    ViewPager viewpager;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("친구"));
        tabs.addTab(tabs.newTab().setText("채팅"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //Adapter
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapter myPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(myPagerAdapter);

        //탭 선택 이벤트
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("친구");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

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
                Intent intent = new Intent(ListActivity.this, Chat_invite_main.class);
                startActivity(intent);
                // 채팅방 만드는 화면 넣을것
                break;
            case R.id.shared_settings:
                Intent intent1 = new Intent(ListActivity.this, SettingActivity.class);
                startActivity(intent1);
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


    public void Friends_list() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/friends/friend_fetch.php", hostname);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("ucAreaNo", ((TextView) findViewById(R.id.edtUserAreaNo)).getText().toString());
            urlBuilder.addQueryParameter("ucDistribId", ((TextView) findViewById(R.id.edtUserDistribId)).getText().toString());
            urlBuilder.addQueryParameter("ucAgencyId", ((TextView) findViewById(R.id.edtUserAgencyId)).getText().toString());
            urlBuilder.addQueryParameter("ucMemCourId", ((TextView) findViewById(R.id.edtUserCourId)).getText().toString());

            Request request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        final ErrorDto error = new Gson().fromJson(response.body().string(), ErrorDto.class);
                        Log.i("tag", error.message);
                        // 응답 실패
                        Log.i("tag", "응답실패");
                    } else {
                        // 응답 성공
                        Log.i("tag", "응답 성공");
                        final String responseData = response.body().string();
                        final FriendsResponse friendsResponse = new Gson().fromJson(responseData, FriendsResponse.class);
                        final FriendsResponse.Items items = new Gson().fromJson(responseData, FriendsResponse.Items.class);
                        final FriendsResponse.Root Root = new Gson().fromJson(responseData, FriendsResponse.Root.class);

                        runOnUiThread(() -> {
                            try {
                                item.add(friendsResponse.acRealName);

                                Log.i("tag", friendsResponse.acRealName);

//                                Fadapter.notifyDataSetChanged();

                                Toast.makeText(getApplicationContext(), "응답 : " + friendsResponse.acRealName, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } // end of else
                } // end of onResponse
            });   // end of callback
        } catch (Exception e) {}
    }             // end of method

}
