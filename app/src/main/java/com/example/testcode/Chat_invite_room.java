package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

/**
 * 채팅창 화면 (Chat)에서 우측 상단 새로운 채팅방 만들기 버튼.
 * 내비게이션 드로어를 넣어서 카카오톡 방식으로 서랍식 구현 할 예정.
 */

public class Chat_invite_room extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_invite);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("대화상대 초대");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        Frag1 frag1 = new Frag1();

        frag1.getContext();


    }
}