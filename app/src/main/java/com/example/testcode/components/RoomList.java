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
import com.example.testcode.databinding.TestRoomlistBinding;
import com.example.testcode.f1Adapter;
import com.example.testcode.f2Adapter;
import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoomList extends FrameLayout {
    private TestRoomlistBinding binding;
    com.example.testcode.f2Adapter f2Adapter;
    ArrayList<DataItem2> chat_room = new ArrayList<>();

    public RoomList(@NonNull Context context) {
        super(context);
        initView();

    }

    public RoomList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public RoomList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    public RoomList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        binding = TestRoomlistBinding.inflate((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE), null, false);

        binding.rvList1.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager manager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvList1.setLayoutManager(manager);

        f2Adapter = new f2Adapter(chat_room);
        binding.rvList1.setAdapter(f2Adapter);

        addView(binding.wrapper);
    }

    public void setOnItemClickListener(com.example.testcode.f2Adapter.OnItemClickListener listener) {
        f2Adapter.setOnItemClickListener(listener);
    }

    public void fetchRoomList (String ucAreaNo, String ucDistribId, String ucAgencyId, String ucMemCourId) {
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.rooms(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId)
                    .enqueue(new retrofit2.Callback<Chat_room_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Chat_room_Response> call,
                                               retrofit2.Response<Chat_room_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Chat_room_Response chat_room_response = response.body();

                                    List<Chat_room_Response.Items.Rooms> items = chat_room_response.items.astRooms;
                                    for (int i = 0; i < items.size(); i++) {
                                        chat_room.add(new DataItem2(items.get(i).acRoomTitle, items.get(i).uiRoomNo));

//                                        SharedPreferences sp = getActivity().getPreferences(MODE_PRIVATE);
//                                        SharedPreferences.Editor editor = sp.edit();
//                                        editor.putString("roomNo", items.get(i).uiRoomNo);
//                                        editor.commit();

                                    }
                                    f2Adapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
//                                    Log.i("tag", error.message);
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<Chat_room_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
