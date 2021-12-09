package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityChatFriendsBinding;
import com.example.testcode.model.Chat_Response;
import com.example.testcode.model.Chat_invite_room_Response;
import com.example.testcode.model.Code;
import com.example.testcode.model.DataItem;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.MyAdapter;
import com.example.testcode.model.PollingChat_Response;
import com.example.testcode.model.Send_msg_Response;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 채팅창 화면.
 */

public class Chat_friends extends AppCompatActivity {
    ArrayList<DataItem> dataList = new ArrayList<>();
    MyAdapter adapter;
    ActionBar actionBar;
    public List<PollingChat_Response.Items.Rooms> items;
    Timer timer;
    Handler handler;

    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, name, getUiRoomNo, testUiRoomNo, uiTalkNo, testAcRoomTitle;

    ActivityChatFriendsBinding binding;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        testUiRoomNo = getIntent().getStringExtra("uiRoomNo");
        testAcRoomTitle = getIntent().getStringExtra("acRoomTitle");
        actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle(testAcRoomTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyvlerv.setLayoutManager(manager);
        adapter = new MyAdapter(dataList);
        binding.recyvlerv.setAdapter(adapter);

        SharedPreferences sharedPreferences= getSharedPreferences("test", MODE_PRIVATE);
        ucAreaNo = sharedPreferences.getString("ar","");
        ucDistribId = sharedPreferences.getString("di","");
        ucAgencyId = sharedPreferences.getString("ag","");
        ucMemCourId = sharedPreferences.getString("me","");
        name = sharedPreferences.getString("name","");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
                
            case R.id.list:
                Intent intent0 = new Intent(Chat_friends.this, ListTest.class);
                intent0.putExtra("RoomNo", testUiRoomNo);
                startActivity(intent0);
                break;

            case R.id.invite:
                Intent intent = new Intent(Chat_friends.this, Chat_invite_room.class);
                intent.putExtra("RoomNo", testUiRoomNo);
                startActivity(intent);
                break;

            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("채팅방을 나가시겠습니까?")
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
                    }
                });
                AlertDialog alert = builder.create();

                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 전송 버튼을 눌렀을 때
    public void onClick_sendmsg(View view) {
        if (binding.sendMsg.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "메시지를 입력하세요.", Toast.LENGTH_SHORT).show();
        } else {
            send_msg();
        }
        binding.sendMsg.setText("");
    }

    //        binding.sendMsg.setOnClickListener {
//            String message = messageDto (
//                uiRoomNo = SharedPreferences로 받아오는 RoomNo,
//                ucAreaNo = 로그인한 id~,
//                ucDistribId = 로그인한 id~,
//                ucAgencyId = 로그인한 id~,
//                ucMemCourId = 로그인한 id~,
//                acTalkMesg = binding.sendMsg.getText().toString() // EditText에 작성해서 전송할 메시지
//            )
//            websocket.send(Gson().toJson(message))
//            binding.sendMsg.setText("");
//        }

    public void send_msg(){
        try {

            final String message = binding.sendMsg.getText().toString();
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.send_msg(
                    testUiRoomNo,
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId,
                    message)
                    .enqueue(new retrofit2.Callback<Send_msg_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Send_msg_Response> call,
                                               retrofit2.Response<Send_msg_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Send_msg_Response send_msg_response = response.body();

                                    String asd = send_msg_response.acDateTime;

                                    dataList.add(new DataItem(
                                            send_msg_response.uiTalkNo,
                                            message,
                                            "", Code.ViewType.RIGHT_CONTENT, asd.substring(10, 16)));
                                    adapter.notifyDataSetChanged();
                                    binding.recyvlerv.scrollToPosition(dataList.size() - 1);

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

    public void get_polling_talk() {
        try {
            // 방어코드.
            final int uiTalkNo = adapter.getItemCount() > 0 ? adapter.getLastItem().getTalkId() : 0;

            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.pollingchat(
                    uiTalkNo,
                    testUiRoomNo,
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

                                     items = pollingChat_response.items.astRooms;

                                    for (int i = 0; i < items.size(); i++) {
                                        if (!ucMemCourId.equals(pollingChat_response.items.astRooms.get(i).ucMemCourId)) {
                                            // dataList (uiTalkNo, acTalkMesg, acRealName, ViewType.CONTENT)
                                            dataList.add(new DataItem(
                                                    items.get(i).uiTalkNo,
                                                    items.get(i).acTalkMesg,
                                                    items.get(i).acRealName, Code.ViewType.LEFT_CONTENT,
                                                    items.get(i).acDateTime.substring(10, 16)));
                                        } else {
                                            dataList.add(new DataItem(
                                                    items.get(i).uiTalkNo,
                                                    items.get(i).acTalkMesg,
                                                    name, Code.ViewType.RIGHT_CONTENT,
                                                    items.get(i).acDateTime.substring(10, 16)));
                                        }
                                    }
                                    adapter.notifyDataSetChanged();

                                    if (items.size() > 0) {
                                        if (items.get(items.size() - 1).uiTalkNo > uiTalkNo) {
                                            binding.recyvlerv.scrollToPosition(dataList.size() - 1);
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

    public void User_delete(){
        try {

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

                                    Intent intent1 = new Intent(Chat_friends.this, ListActivity.class);
                                    startActivity(intent1);

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

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer(true); //인자가 Daemon 설정인데 true 여야 죽지 않음.
        handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable(){
                    public void run(){
                        get_polling_talk();
                    }
                });
            }
        }, 0, 500);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

/*
MyApplication -> Application SIngleton
MyApplication -> Socket Service
Ping Pong Socket Ping/Pong
socket.send(ping~)
socket.send(pong~)

// Timer
MyApplication -> 5s 10s
// Application Timer -> Socket Service call
SocketService.ensureAlive();

// Application Timer -> SocketService Call
fun ensureALive() {
socket.send(ping)
lastSentPing = System.currentTimeMills();
}

// SocketSevice timer
fun ensureSocketAlive() {
if ( lastSentPing + 1_000*10 < System.currentTimeMills() ) {
socket.close()
socket.connect()
}
}
 */