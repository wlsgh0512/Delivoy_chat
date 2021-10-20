package com.example.testcode;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.FragmentFrag1Binding;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 친구 탭 -> 친구 목록 조회.
 * ListView에 데이터 받아와서 .add()하는 식으로 구현하려 했으나 갱신 x.
 * Fragment 사용.
 * onResponse에서 response.body()가 null
 * json 데이터의 객체는 {} , 배열은 [].
 */

public class Frag1 extends Fragment {
    ArrayList<DataItem2> friends_name = new ArrayList<>();
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId;

    FragmentFrag1Binding binding;

    f1Adapter f1Adapter;
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

        binding.listview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager manager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.listview.setLayoutManager(manager);

        f1Adapter = new f1Adapter(friends_name);
        binding.listview.setAdapter(f1Adapter);

        return binding.getRoot();
        // Inflate the layout for this fragment
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        f1Adapter.setOnItemClickListener(new f1Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), Chat_friends.class);
                intent.putExtra("uiRoomNo", position + 1);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("test", MODE_PRIVATE);
        ucAreaNo = sharedPreferences.getString("ar","");
        ucDistribId = sharedPreferences.getString("di","");
        ucAgencyId = sharedPreferences.getString("ag","");
        ucMemCourId = sharedPreferences.getString("me","");

        Friends_list();

    }

    public void Friends_list() {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.friend(
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId
            )
                    .enqueue(new retrofit2.Callback<FriendsResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<FriendsResponse> call,
                                               retrofit2.Response<FriendsResponse> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    FriendsResponse friendsResponse = response.body();

                                    List<FriendsResponse.Items.Rooms> items = friendsResponse.items.astRooms;
                                    for(int i = 0 ; i < items.size(); i++) {
                                        friends_name.add(new DataItem2(items.get(i).acRealName));
                                    }

                                    f1Adapter.notifyDataSetChanged();

//                                    Intent intent7 = new Intent(getActivity(), Create_chatroom.class);
//                                    intent7.putExtra("friends_name", friends_name);
//                                    startActivity(intent7);

                                    // 코틀린 friends_name.addAll(friendsResponse.items.astRooms.map(it::name))
                                    // friends_name.add(friendsResponse.items.astRooms.stream().map(it -> it.name));
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

}



