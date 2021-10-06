package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * 채팅 메인 화면 (ListActivity)에서 우측 상단 새로운 채팅방 만들기 버튼.
 *
 */

public class Chat_invite_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_invite_main);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("새로운 채팅방 만들기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        Frag1 frag1 = new Frag1();

        frag1.getContext();

    }
}