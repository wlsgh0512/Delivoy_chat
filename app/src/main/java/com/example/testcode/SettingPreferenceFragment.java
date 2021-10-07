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
 * settings_preference.xml -> 아이디 부분 summary에 어떻게 아이디 표현 ??
 */

public class SettingPreferenceFragment extends PreferenceFragment {
    SharedPreferences prefs;

    ListPreference soundPreference;
    ListPreference keywordSoundPreference;
    PreferenceScreen keywordScreen;


    String hostname = "222.239.254.253";
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, user_id, name;

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

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("test", Context.MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
                ucAreaNo = sharedPreferences.getString("ar","");
                ucDistribId = sharedPreferences.getString("di","");
                ucAgencyId = sharedPreferences.getString("ag","");
                ucMemCourId = sharedPreferences.getString("me","");
                user_id = sharedPreferences.getString("id","");
                name = sharedPreferences.getString("name","");

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
//                user_id.setSummary("ar" + "-" + "di" + "-" + "ag" + "-" + "me");
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
                Member_secession();
                break;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public void Member_secession() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/users/user_put.php", hostname);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ucAreaNo", ucAreaNo)
                    .addFormDataPart("ucDistribId",ucDistribId)
                    .addFormDataPart("ucAgencyId",ucAgencyId)
                    .addFormDataPart("ucMemCourId",ucMemCourId)
                    .addFormDataPart("acUserId",user_id)
                    .addFormDataPart("acPassword","11111111")
                    .addFormDataPart("acRealName",name)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "x-www-form-urlencoded")
                    .post(requestBody)
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
                        final Secession_Response secession_response = new Gson().fromJson(responseData, Secession_Response.class);
                        getActivity().runOnUiThread(() -> {
                            try {
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