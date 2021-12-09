package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.databinding.ActivityChatFriendsBinding;
import com.example.testcode.databinding.ActivityTextSizeBinding;

/**
 * 텍스트 크기 조절하기.
 * 로드보이 및 구글링 참조해서 구현하려 했으나
 * SeekBar로 조정한 글자 크기를 실제로 적용하는 것은 하지 못했습니다..
 * 로드보이처럼 SeekBar로 글자 크기 조정을 5% 단위로 움직이는 것 구현 목표.
 */

public class TextSize extends AppCompatActivity {

    ActivityTextSizeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextSizeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int size = 10;
        binding.sbTextSize.setMax(20);
        binding.sbTextSize.setProgress(size);
        binding.tvTextSizePreview.setTextSize((float)10);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("텍스트 크기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        binding.sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                binding.tvTextSizePreview.setTextSize(progress);
                int ratio = progress * 5;
                binding.tvTextSize.setText(String.format("%s%%", ratio));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
            case R.id.create_chat:

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public int getRatioByProgress(int progress) {
        progress += 10;
        progress *= 5;
        return progress;
    }
}