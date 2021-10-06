package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Change_pw extends AppCompatActivity {
    EditText input_id, input_phone, key, now_pw, new_pw, new_pw_check;
    Button change_success, key_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        change_success = (Button)findViewById(R.id.change_success);
        key_check = (Button)findViewById(R.id.key_check);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("비밀번호 변경");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기
    }

    public void onClick_change_pw(View view) {
        switch (view.getId()) {
            case R.id.key_check:
                Toast.makeText(this, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            // 휴대전화번호 인증 ~ 부분
                break;
            case R.id.change_success:
                Toast.makeText(this, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Change_pw.this, ListActivity.class);
                startActivity(intent);
                break;

        }

    }
}