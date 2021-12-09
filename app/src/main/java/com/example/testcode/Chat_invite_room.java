package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityChatInviteBinding;
import com.example.testcode.model.Chat_invite_room_Response;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 채팅창 화면 (Chat)에서 우측 상단 새로운 채팅방 만들기 버튼.
 * 내비게이션 드로어를 넣어서 카카오톡 방식으로 서랍식 구현 할 예정.
 * 초대할 친구목록 중 참여중인 사람이 중복 선택되면
 */

public class Chat_invite_room extends AppCompatActivity {

    ActivityChatInviteBinding binding;
    String testUiRoomNo, ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId,
            ucFriAreaNo, ucFriDistribId, ucFriAgencyId,ucFriMemCourId, invite_mem,
            selectName, selectMemCour;

    public List<FriendsResponse.Items.Rooms> items;

    public ArrayList<String> inviteRoomUser = new ArrayList<String>();

    private Set<FriendsResponse.Items.Rooms> All = new HashSet<FriendsResponse.Items.Rooms>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatInviteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("방에 친구 초대");  //액션바 제목설정
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        testUiRoomNo = getIntent().getStringExtra("RoomNo");

        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");

//        SharedPreferences sharedPreferences1 = getSharedPreferences("zzzz", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
//        ucFriAreaNo = sharedPreferences1.getString("far", "");
//        ucFriDistribId = sharedPreferences1.getString("fd", "");
//        ucFriAgencyId = sharedPreferences1.getString("fag", "");
//        ucFriMemCourId = sharedPreferences1.getString("fm", "");

        SharedPreferences prefs = getSharedPreferences("joinName", MODE_PRIVATE);
        int size = prefs.getInt("Status_size", 0);
        for(int i=0;i<size;i++)
        {
            inviteRoomUser.add(prefs.getString("Status_" + i, null));
        }

        binding.friendList.fetchFriendList(ucAreaNo,ucDistribId,ucAgencyId,ucMemCourId);

        binding.friendList.setOnItemClickListener(new f1Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                final FriendsResponse.Items.Rooms data = binding.friendList.items.get(position);
                v.setBackgroundColor(All.contains(data) ? Color.LTGRAY : Color.WHITE);
                if ( All.contains(data))
                    All.remove(data);
                else
                    All.add(data);

                selectName = binding.friendList.items.get(position).acRealName;
                binding.inviteUser.setText(selectName);
                selectMemCour = binding.friendList.items.get(position).ucMemCourId;

                SharedPreferences spf = getSharedPreferences("zxcv",MODE_PRIVATE);
                SharedPreferences.Editor edi = spf.edit();
                edi.putString("aa", binding.friendList.items.get(position).ucAreaNo);
                edi.putString("bb", binding.friendList.items.get(position).ucDistribId);
                edi.putString("cc", binding.friendList.items.get(position).ucAgencyId);
                edi.putString("select", selectMemCour);
                edi.commit();
            }
        });
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
                if(inviteRoomUser.contains(binding.inviteUser.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "이미 대화방에 참여중인 사용자입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Chat_invite_room();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void Chat_invite_room() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("zxcv", MODE_PRIVATE);
            invite_mem = sharedPreferences.getString("select", "");

//            final String newUser = binding.findList.getText().toString();

            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.room_invite(
                    testUiRoomNo,
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    invite_mem)
                    .enqueue(new retrofit2.Callback<Chat_invite_room_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Chat_invite_room_Response> call,
                                               retrofit2.Response<Chat_invite_room_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Chat_invite_room_Response chat_invite_room_response = response.body();

                                    Toast.makeText(getApplicationContext(), selectName + "를 초대 했습니다.", Toast.LENGTH_SHORT).show();

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
                        public void onFailure(retrofit2.Call<Chat_invite_room_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}