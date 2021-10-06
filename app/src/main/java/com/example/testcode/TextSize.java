package com.example.testcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 텍스트 크기 조절하기.
 * 로드보이 및 구글링 참조해서 구현하려 했으나
 * SeekBar로 조정한 글자 크기를 실제로 적용하는 것은 하지 못했습니다..
 * 로드보이처럼 SeekBar로 글자 크기 조정을 5% 단위로 움직이는 것 구현 목표.
 */

public class TextSize extends AppCompatActivity {

    TextView tvTextSizePreview;
    SeekBar sbTextSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_size);

        tvTextSizePreview = (TextView) findViewById(R.id.tvTextSizePreview);
        sbTextSize = (SeekBar) findViewById(R.id.sbTextSize);

        int size = 13;
        sbTextSize.setMax(25);
        sbTextSize.setProgress(size);
        tvTextSizePreview.setTextSize((float)13);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("텍스트 크기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tvTextSizePreview.setTextSize(progress);

                // seekbar 조절만 되고 옆에 textview 조절한 값 settext 안됨
                // 실제 글씨 크기 바꾸는 것 적용 안됨.
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}