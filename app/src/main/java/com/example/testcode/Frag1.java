package com.example.testcode;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ActivityMainBinding;
import com.example.testcode.databinding.FragmentFrag1Binding;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.Join_Response;
import com.example.testcode.model.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 친구 탭 -> 친구 목록 조회.
 * ListView에 데이터 받아와서 .add()하는 식으로 구현하려 했으나 갱신 x.
 * Fragment 사용.
 * onResponse에서 response.body()가 null
 * json 데이터의 객체는 {} , 배열은 [].
 */

public class Frag1 extends Fragment {
    ArrayList<String> friends_name = new ArrayList<String>();
    private ArrayAdapter Fadapter;
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId;

    FragmentFrag1Binding binding;

    //    public static Context context_frag1;
    public Frag1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFrag1Binding.inflate(getLayoutInflater());

//        Friends_list();
//        friends_name.add("asd");
//        test();
        return binding.getRoot();
        // Inflate the layout for this fragment
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Fadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, friends_name);
        binding.listview.setAdapter(Fadapter);

        // listview를 클릭했을 때 채팅방으로 넘어가도록,,
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> apterView, View view, int i, long l) {

                Friends_list();
//                Intent intent = new Intent(getActivity(), Chat_friends.class);
//                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar","");
        ucDistribId = sharedPreferences.getString("di","");
        ucAgencyId = sharedPreferences.getString("ag","");
        ucMemCourId = sharedPreferences.getString("me","");

//        test();
        friends_name.add("asd");

//        Friends_list();

    }

    public void Friends_list() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.friend(
//                    ucAreaNo,
//                    ucDistribId,
//                    ucAgencyId,
//                    ucMemCourId
                    "88","17","1","1"
            )
                    .enqueue(new retrofit2.Callback<FriendsResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<FriendsResponse> call,
                                               retrofit2.Response<FriendsResponse> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final FriendsResponse friendsResponse = response.body();
                                    friends_name.add(friendsResponse.acRealName);
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
                        public void onFailure(retrofit2.Call<FriendsResponse> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        getActivity().runOnUiThread(() -> {
            try {
                friends_name.add("asdasd");
                Toast.makeText(getActivity().getApplicationContext(), "응답 : ", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }


}



