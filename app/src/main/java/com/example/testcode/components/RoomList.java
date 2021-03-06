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
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testcode.Chat_friends;
import com.example.testcode.R;
import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.FriendListBinding;
import com.example.testcode.databinding.TestRoomlistBinding;
import com.example.testcode.f1Adapter;
import com.example.testcode.f2Adapter;
import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.DataItem;
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
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId;

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

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("test", MODE_PRIVATE);    // test ????????? ???????????? ??????, ?????? test key?????? ????????? ?????? ?????? ?????????.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");

        binding.wrapperr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* swipe ??? ????????? ?????? */
                chat_room.clear();
                fetchRoomList(ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId);
                f2Adapter.notifyDataSetChanged();


                /* ??????????????? ???????????? ?????? */
                binding.wrapperr.setRefreshing(false);
            }
        });

        addView(binding.wrapperr);

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
                                // ?????? ??????
                                Log.i("tag", "?????? ??????");
                                try {
                                    final Chat_room_Response chat_room_response = response.body();

                                    List<Chat_room_Response.Items.Rooms> items = chat_room_response.items.astRooms;
                                    for (int i = 0; i < items.size(); i++) {
                                        chat_room.add(new DataItem2(items.get(i).acRoomTitle, items.get(i).uiRoomNo, ""));
//                                        f2Adapter.notifyItemInserted(i);
                                    }
                                    f2Adapter.notifyDataSetChanged();

                                    SharedPreferences sp= getContext().getSharedPreferences("roomName", MODE_PRIVATE);
                                    SharedPreferences.Editor mEdit1= sp.edit();
                                    mEdit1.putInt("Status_size",chat_room.size());
                                    for(int i = 0; i < chat_room.size(); i++)
                                    {
                                        mEdit1.remove("Status_" + i);
                                        mEdit1.putString("Status_" + i, chat_room.get(i).getContent());
                                    }
                                    mEdit1.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
//                                    Log.i("tag", error.message);
                                    // ?????? ??????
                                    Log.i("tag", "????????????");
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
