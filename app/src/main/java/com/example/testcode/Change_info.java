package com.example.testcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityChangeInfoBinding;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.Join_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 채팅 메인 -> 3dot 정보수정.
 * postman에서 실제로 데이터가 바뀌는거 확인.
 * 여기서도 dialog 뜨기전에 죽는데 일단 데이터는 바뀌어서 다른거부터.
 * 1개라도 변경된 것이 있어야 확인 버튼 눌렀을 때 server로 전송되게 할 것. (변경 x -> 종료)
 * 변경은 되지만 휴대전화번호 인증키 미구현.
 */

public class Change_info extends AppCompatActivity {
    String hostname = "222.239.254.253";
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, user_id, name, uc;

    private ActivityChangeInfoBinding binding;

    private static final String HASH_TYPE = "SHA-256";
    public static final int NUM_HASHED_BYTES = 9;
    public static final int NUM_BASE64_CHAR = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("정보 수정");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        SharedPreferences sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");
        user_id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        uc = sharedPreferences.getString("uc", "");



        binding.inputMemberNum.setText(ucAreaNo + "-" + ucDistribId + "-" + ucAgencyId + "-" + ucMemCourId);
        binding.inputUserId.setText(user_id);
        binding.realName.setText(name);

        binding.keyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_Sms();
                checkPermission();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 확인 버튼
    public void onClick_finish(View view) {
        final String nickName = binding.inputNickname.getText().toString();
        final String email = binding.inputEmailAddress.getText().toString();
        if (nickName.isEmpty() || email.isEmpty()){
            Toast.makeText(getApplicationContext(), "입력된 변경 사항이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Change_user_info();
        }
    }


    public void Change_user_info() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);

            service.change(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId,
                    binding.inputNickname.getText().toString(),
                    binding.inputPhoneNumber.getText().toString(),
                    binding.inputEmailAddress.getText().toString(),
                    uc)

                    .enqueue(new retrofit2.Callback<Join_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Join_Response> call,
                                               retrofit2.Response<Join_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Join_Response join_response = response.body();

                                    Toast.makeText(getApplicationContext(), "정보수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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
                        public void onFailure(retrofit2.Call<Join_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send_Sms(){
        SmsManager sms = SmsManager.getDefault();
        String phoneNumber="01099778981";
        String key_hash = getAppSignatures(this);
        String message ="<#> XXX 앱의 인증번호는 [123456] 입니다.메세지테스트\n"+key_hash;
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void checkPermission() {
        String[] permission_list = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS
        };

        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for (String permission : permission_list) {
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);
            if (chk == PackageManager.PERMISSION_DENIED) {
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list, 0);
            }
        }
    }

    public static String getAppSignatures(Context context) {
        ArrayList<String> appCodes = new ArrayList<>();
        String hash ="";
        try {
            // Get all package signatures for the current package
            String packageName = context.getPackageName();
            PackageManager packageManager = context.getPackageManager();
            Signature[] signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;

            // For each signature create a compatible hash
            for (Signature signature : signatures) {
                hash = getHash(packageName, signature.toCharsString());
                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                }
                Log.d("tag", String.format("이 값을 SMS 뒤에 써서 보내주면 됩니다 : %s", hash));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("tag", "Unable to find package to obtain hash. : " + e.toString());
        }
        return hash;
    }

    private static String getHash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_TYPE);
            // minSdkVersion이 19이상이면 체크 안해도 됨
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));
            }
            byte[] hashSignature = messageDigest.digest();

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES);
            // encode into Base64
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR);

            Log.d("tag", String.format("\nPackage : %s\nHash : %s", packageName, base64Hash));
            return base64Hash;
        } catch (NoSuchAlgorithmException e) {
            Log.d("tag", "hash:NoSuchAlgorithm : " + e.toString());
        }
        return null;
    }

}