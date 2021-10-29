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
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityChatFriendsBinding;
import com.example.testcode.model.Chat_Response;
import com.example.testcode.model.Code;
import com.example.testcode.model.DataItem;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.MyAdapter;
import com.example.testcode.model.PollingChat_Response;
import com.example.testcode.model.Send_msg_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 채팅창 화면.
 */

public class Chat_friends extends AppCompatActivity {
    String msg = "";
    ArrayList<DataItem> dataList = new ArrayList<>();
    MyAdapter adapter;
    ActionBar actionBar;
    List<PollingChat_Response.Items.Rooms> items;
    Timer timer;
    Handler handler;

    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, name, getUiRoomNo, testUiRoomNo, uiTalkNo;

    ActivityChatFriendsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        msg = binding.sendMsg.getText().toString();

//        get_talk();

        testUiRoomNo = getIntent().getStringExtra("uiRoomNo");

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyvlerv.setLayoutManager(manager);
        adapter = new MyAdapter(dataList);
        binding.recyvlerv.setAdapter(adapter);

        //binding.recyvlerv.setAdapter(new MyAdapter(dataList));  // Adapter 등록

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        SharedPreferences sharedPreferences= getSharedPreferences("test", MODE_PRIVATE);
        ucAreaNo = sharedPreferences.getString("ar","");
        ucDistribId = sharedPreferences.getString("di","");
        ucAgencyId = sharedPreferences.getString("ag","");
        ucMemCourId = sharedPreferences.getString("me","");
        name = sharedPreferences.getString("name","");


//        timer = new Timer(true); //인자가 Daemon 설정인데 true 여야 죽지 않음.
//        handler = new Handler();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable(){
//                    public void run(){
//                        get_polling_talk();
//                        binding.recyvlerv.scrollToPosition(dataList.size() - 1);
//                    }
//                });
//            }
//        }, 0, 100);

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

                        Intent intent1 = new Intent(Chat_friends.this, ListActivity.class);
                        startActivity(intent1);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyvlerv.scrollToPosition(dataList.size() - 1);
            }
        }, 200);
        send_msg();
        binding.sendMsg.setText("");


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

                                    dataList.add(new DataItem(
                                            send_msg_response.uiTalkNo,
                                            message,
                                            "", Code.ViewType.RIGHT_CONTENT));
                                    binding.recyvlerv.getAdapter().notifyDataSetChanged();
                                    adapter.notifyDataSetChanged();

//                                    SharedPreferences sp = getPreferences(MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sp.edit();
//                                    editor.putString("TalkNo", String.valueOf(send_msg_response.uiTalkNo));
//                                    editor.commit();

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


//    public void get_talk() {
//        try {
//            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
//            service.chat(adapter.getLastItem().getTalkId(),
//                    testUiRoomNo,
//                    ucAreaNo,
//                    ucDistribId,
//                    ucAgencyId,
//                    ucMemCourId)
//                    .enqueue(new retrofit2.Callback<Chat_Response>() {
//                        @Override
//                        public void onResponse(retrofit2.Call<Chat_Response> call,
//                                               retrofit2.Response<Chat_Response> response) {
//                            if (response.isSuccessful()) {
//                                // 응답 성공
//                                Log.i("tag", "응답 성공");
//                                try {
//                                    final Chat_Response chat_response = response.body();
//
//                                    for (int i = 0; i < chat_response.uiTalkNo.length(); i ++) {
//                                        if (ucMemCourId != chat_response.ucMemCourId) {
//                                            dataList.add(new DataItem(chat_response.uiTalkNo, chat_response.acTalkMesg, chat_response.acRealName, Code.ViewType.LEFT_CONTENT));
//                                            actionBar.setTitle(chat_response.acRealName);
//                                        }
//                                    }
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                final ErrorDto error;
//                                try {
//                                    error = new Gson().fromJson(response.errorBody().string(),
//                                            ErrorDto.class);
//                                    Log.i("tag", error.message);
//                                    // 응답 실패
//                                    Log.i("tag", "응답실패");
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(retrofit2.Call<Chat_Response> call, Throwable t) {
//
//                        }
//                    });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void get_polling_talk() {
        try {
//            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
//            uiTalkNo = sharedPreferences.getString("TalkNo", "");

//            final int uiTalkNo = adapter.getLastItem().getTalkId() -1 ;

            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.pollingchat(
//                    uiTalkNo,
                    0,
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
                                                    items.get(i).acRealName, Code.ViewType.LEFT_CONTENT));
                                        } else {
                                            dataList.add(new DataItem(
                                                    items.get(i).uiTalkNo,
                                                    items.get(i).acTalkMesg,
                                                    name, Code.ViewType.RIGHT_CONTENT));
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
                        binding.recyvlerv.scrollToPosition(dataList.size() - 1);
                    }
                });
            }
        }, 0, 100);
    }






}