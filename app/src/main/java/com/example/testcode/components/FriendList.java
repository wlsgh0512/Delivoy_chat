package com.example.testcode.components;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testcode.Chat_friends;
import com.example.testcode.R;
import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.FriendListBinding;
import com.example.testcode.f1Adapter;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendList extends FrameLayout {
    private FriendListBinding binding;
    com.example.testcode.f1Adapter f1Adapter;
    public ArrayList<DataItem2> friends_name = new ArrayList<>();
    public List<FriendsResponse.Items.Rooms> items;

    public FriendList(@NonNull Context context) {
        super(context);
        initView();

    }

    public FriendList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public FriendList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    public FriendList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        binding = FriendListBinding.inflate((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE), null, false);

        binding.rvList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager manager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvList.setLayoutManager(manager);

        f1Adapter = new f1Adapter(friends_name);
        binding.rvList.setAdapter(f1Adapter);

        addView(binding.wrapper);
    }

    public void setOnItemClickListener(com.example.testcode.f1Adapter.OnItemClickListener listener) {
        f1Adapter.setOnItemClickListener(listener);
    }


    public void fetchFriendList(String ucAreaNo, String ucDistribId, String ucAgencyId, String ucMemCourId) {
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

                                    items = friendsResponse.items.astRooms;
                                    for(int i = 0 ; i < items.size(); i++) {
                                        friends_name.add(new DataItem2(items.get(i).acRealName, ""));

                                    }
                                    f1Adapter.notifyDataSetChanged();

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
