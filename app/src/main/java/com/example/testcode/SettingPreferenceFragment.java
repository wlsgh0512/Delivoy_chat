package com.example.testcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SettingPreferenceFragment extends PreferenceFragment {
    SharedPreferences prefs;

    ListPreference soundPreference;
    ListPreference keywordSoundPreference;
    PreferenceScreen keywordScreen;

    String hostname = "222.239.254.253";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
//        soundPreference = (ListPreference) findPreference("sound_list");

//        keywordSoundPreference = (ListPreference)findPreference("keyword_sound_list");

//        keywordScreen = (PreferenceScreen)findPreference("keyword_screen");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!prefs.getString("sound_list", "").equals("")) {
            soundPreference.setSummary(prefs.getString("sound_list", "카톡"));
        }

        if (!prefs.getString("keyword_sound_list", "").equals("")) {
            keywordSoundPreference.setSummary(prefs.getString("keyword_sound_list", "카톡"));
        }

        if (prefs.getBoolean("keyword", false)) {
            keywordScreen.setSummary("사용");
        }

        prefs.registerOnSharedPreferenceChangeListener(prefListener);

    }// onCreate

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("sound_list")) {
                soundPreference.setSummary(prefs.getString("sound_list", "카톡"));
            }

            if (key.equals("keyword_sound_list")) {
                keywordSoundPreference.setSummary(prefs.getString("keyword_sound_list", "카톡"));
            }

            if (key.equals("keyword")) {

                if (prefs.getBoolean("keyword", false)) {
                    keywordScreen.setSummary("사용");

                } else {
                    keywordScreen.setSummary("사용안함");
                }

                //2뎁스 PreferenceScreen 내부에서 발생한 환경설정 내용을 2뎁스 PreferenceScreen에 적용하기 위한 소스
                ((BaseAdapter) getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
            }

        }
    };

    @Override
    public boolean onPreferenceTreeClick(android.preference.PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();

        switch (key) {
            case "user_id":
                
                break;
            case "user_pw":
                Intent intent = new Intent(getActivity(), Change_pw.class);
                startActivity(intent);
                break;
            case "text_size":
                Intent intent1 = new Intent(getActivity(), TextSize.class);
                startActivity(intent1);
                break;
            case "secession":
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("정말로 탈퇴하시겠습니까?\n탈퇴한 회원은 복구할 수 없습니다.")
                        .setCancelable(true)
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public void getOldLogin() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/login/login_no_get.php", hostname);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("ucAreaNo", ((TextView) getView().findViewById(R.id.edtUserAreaNo)).getText().toString());
            urlBuilder.addQueryParameter("ucDistribId", ((TextView) getView().findViewById(R.id.edtUserDistribId)).getText().toString());
            urlBuilder.addQueryParameter("ucAgencyId", ((TextView) getView().findViewById(R.id.edtUserAgencyId)).getText().toString());
            urlBuilder.addQueryParameter("ucMemCourId", ((TextView) getView().findViewById(R.id.edtUserCourId)).getText().toString());

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
                        final LoginResponse loginResponse = new Gson().fromJson(responseData, LoginResponse.class);
                        getActivity().runOnUiThread(() -> {
                            try {
                                // 아이디 비밀번호 일치 시 로그인 성공하게 하는 조건?
                                Toast.makeText(getActivity().getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();

//                                Toast.makeText(getApplicationContext(), "응답" + loginResponse.ucAreaNo + loginResponse.ucDistribId + loginResponse.ucAgencyId + loginResponse.ucMemCourId , Toast.LENGTH_SHORT).show();
//                                Log.d("tag", loginResponse.ucAgencyId);
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
}