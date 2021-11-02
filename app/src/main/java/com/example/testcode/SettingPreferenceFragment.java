package com.example.testcode;

import android.content.Context;
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

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.Join_Response;
import com.example.testcode.model.LoginResponse;
import com.example.testcode.model.Secession_Response;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * settings_preference.xml -> 아이디 부분 summary에 어떻게 아이디 표현 - 해
 * 메시지 알림, 진동, 소리, 알림음, 텍스트 크기 현재는 내용 구현이 안 되어 있음.
 */

public class SettingPreferenceFragment extends PreferenceFragment {
    SharedPreferences prefs;

    ListPreference soundPreference;
    ListPreference keywordSoundPreference;
    PreferenceScreen keywordScreen;

    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, user_id, name, pw;

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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("test", Context.MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");
        user_id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        pw = sharedPreferences.getString("pw", "");

        findPreference("user_id").setSummary(ucAreaNo + "-" + ucDistribId + "-" +
                ucAgencyId + "-" + ucMemCourId + "\n" + user_id);

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
//                findPreference("user_id").setSummary(ucAreaNo + "-" + ucDistribId + "-" +
//                        ucAgencyId + "-" + ucMemCourId + "\n" + user_id);
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
                builder.setMessage("정말 회원탈퇴 하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Member_secession();
                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                break;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public void Member_secession() {

        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.secession(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId,
                    user_id,
                    pw,
                    name)
                    .enqueue(new retrofit2.Callback<Secession_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Secession_Response> call,
                                               retrofit2.Response<Secession_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Secession_Response secession_response = response.body();

                                    Toast.makeText(getActivity(), "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

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
                        public void onFailure(retrofit2.Call<Secession_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}