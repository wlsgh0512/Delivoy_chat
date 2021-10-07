package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.testcode.model.Chat_Response;
import com.example.testcode.model.Code;
import com.example.testcode.model.DataItem;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.MyAdapter;
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
 * 채팅창 화면.
 * response.body.string() 값 잘 받아오는것까지는 확인.
 * ArrayList에 add해서 채팅창에 갱신 확인.
 * 나오긴 하는데 화면에 진입했을 때 딱 안나오고 EditText를 한번 눌러야 나오는 상태.
 * 이유를 잘 모르겠어서 추가 검색 필요할 듯.
 */

public class Chat_friends extends AppCompatActivity {
    String hostname = "222.239.254.253";
    RecyclerView recyvlerv;
    EditText sendMsg;
    String msg = "";
    ArrayList<DataItem> dataList;
    ActionBar actionBar;
//    ArrayList<DataItem> dataList = new ArrayList<DataItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_friends);

        actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
//        actionBar.setTitle("채팅");  //액션바 제목설정
        // 1:1이면 상대이름을 ,
        // multi면 그룹채팅 (참여인원) 카톡처럼.

        sendMsg = (EditText) findViewById(R.id.sendMsg);
        msg = sendMsg.getText().toString();
        recyvlerv = (RecyclerView)findViewById(R.id.recyvlerv);

        initializeData();
        talk_get();

//      임의로 값 넣어서 채팅창에 뜨는 것까지 확인.
//      dataList.add(new DataItem("하이루 여포, 동탁", "김동현", Code.ViewType.LEFT_CONTENT));

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyvlerv.setLayoutManager(manager);
        recyvlerv.setAdapter(new MyAdapter(dataList));  // Adapter 등록

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기


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
             * 검색해서 초대(초성 포함) 구현하는 것이 목표.
             */
            case R.id.invite:
                Intent intent = new Intent(Chat_friends.this, Chat_invite_room.class);
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

         msg = sendMsg.getText().toString();
           dataList.add(new DataItem(msg, "사용자1", Code.ViewType.RIGHT_CONTENT));
           recyvlerv.scrollToPosition(dataList.size()-1);
           sendMsg.setText("");

        //
//        talk_get();
    }

    /**
     *  파일 관련 버튼을 눌렀을 때
     *  다이얼로그 방식으로 띄우면 채팅을 방해하는 요소가 되니까
     *  다른 방법 찾을것.
     */
    public void addFile(View view) {

    }

    public void talk_get() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/talks/talk_get.php", hostname);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("uiTalkNo", "1"); // 22
            urlBuilder.addQueryParameter("uiRoomNo", "1"); // 9
            /**
             * 지금은 확인만 하려고 value 값을 임의로 집어넣기.
             */
//            urlBuilder.addQueryParameter("ucAreaNo", ((TextView) findViewById(R.id.edtUserAreaNo)).getText().toString());
//            urlBuilder.addQueryParameter("ucDistribId", ((TextView) findViewById(R.id.edtUserDistribId)).getText().toString());
//            urlBuilder.addQueryParameter("ucAgencyId", ((TextView) findViewById(R.id.edtUserAgencyId)).getText().toString());
//            urlBuilder.addQueryParameter("ucMemCourId", ((TextView) findViewById(R.id.edtUserCourId)).getText().toString());

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
                        final Chat_Response chat_response = new Gson().fromJson(responseData, Chat_Response.class);

                        String abc = chat_response.acTalkMesg;
                        String qwe = chat_response.acRealName;

                        runOnUiThread(() -> {
                            try {

                                dataList.add(new DataItem(chat_response.acTalkMesg, chat_response.acRealName, Code.ViewType.LEFT_CONTENT));
                                actionBar.setTitle(chat_response.acRealName);
//                                Toast.makeText(getApplicationContext(), "" + chat_response.acTalkMesg, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });


                    }
                }
            });
        } catch (Exception e) {
        }
    }

    public void initializeData() {
        dataList = new ArrayList<>();
    }
}