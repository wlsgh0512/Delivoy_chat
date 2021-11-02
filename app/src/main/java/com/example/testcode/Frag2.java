package com.example.testcode;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.ChatroomBinding;
import com.example.testcode.databinding.ChatroomDialogBinding;
import com.example.testcode.databinding.FragmentFrag1Binding;
import com.example.testcode.databinding.FragmentFrag2Binding;
import com.example.testcode.model.Change_roomTitle;
import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.Code;
import com.example.testcode.model.DataItem;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.Delete_Room_Response;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.MyAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 채팅 탭 -> 참여중인 채팅 목록 조회.
 */

public class Frag2 extends Fragment {
    AlertDialog.Builder builder, builder1;
    ArrayList<DataItem2> room_list = new ArrayList<>();
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, RoomNo;
    List<Chat_room_Response.Items.Rooms> items;
    EditText editText;
    f2Adapter f2Adapter;
    Frag2 f2;

    FragmentFrag2Binding binding;
    ChatroomDialogBinding binding1;

    Timer timer;
    Handler handler;

    public Frag2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFrag2Binding.inflate(getLayoutInflater());
        binding1 = ChatroomDialogBinding.inflate(getLayoutInflater());


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");


        binding.roomList.fetchRoomList(ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId);


//        f2Adapter.setOnItemLongClickListener(new f2Adapter.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(View v, int position) {
//
//                builder = new AlertDialog.Builder(getActivity());
//
//                builder.setTitle(room_list.get(position).getContent());
//
//                builder.setItems(R.array.chat_long, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        switch (i) {
//                            case 0:
//                                builder = new AlertDialog.Builder(getActivity());
//                                builder.setTitle("채팅방 이름 변경");
//                                editText = new EditText(getActivity());
////                                builder.setView(R.layout.chatroom_dialog);
//                                builder.setView(editText);
//
//                                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        return;
//                                    }
//                                });
//
//                                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        change_title();
////                                        Log.i("tag", editText.getText().toString());
//                                    }
//                                });
//                                builder.show();
//                                break;
//                            case 1:
//                                // Listview 순서 바꿔주기 후 고정?
//                                break;
//                            case 2:
//                                SharedPreferences spf = getActivity().getPreferences(MODE_PRIVATE);
//                                SharedPreferences.Editor edi = spf.edit();
//                                edi.putInt("room_list_position", position);
//                                edi.commit();
//
//                                delete_room();
//                                Toast.makeText(getActivity().getApplicationContext(), "채팅방이 삭제되었습니다", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.show();
//            }
//        });




        return binding.getRoot();
    }

    public void Chat_room_list() {
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

                                    items = chat_room_response.items.astRooms;
                                    for (int i = 0; i < items.size(); i++) {
                                        room_list.add(new DataItem2(items.get(i).acRoomTitle, items.get(i).uiRoomNo));

                                        SharedPreferences sp = getActivity().getSharedPreferences("bbbb",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("roomNo", items.get(i).uiRoomNo);
                                        editor.commit();

                                    }
                                    f2Adapter.notifyDataSetChanged();


//                                    Intent intent7 = new Intent(getActivity(), Create_chatroom.class);
//                                    intent7.putExtra("room_list",room_list);
//                                    startActivity(intent7);

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

    public void change_title() {
        try {
            SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            RoomNo = sharedPreferences.getString("roomNo", "");

            SharedPreferences sharedPreferences1 = getActivity().getPreferences(MODE_PRIVATE);
            int get_roomlist_pos = sharedPreferences1.getInt("room_list_position", 0);

//            final String title = binding1.addboxdialog.getText().toString();
            final String title = editText.getText().toString();


            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.change(
                    RoomNo,
                    title,
                    ""
            )
                    .enqueue(new retrofit2.Callback<Change_roomTitle>() {
                        @Override
                        public void onResponse(retrofit2.Call<Change_roomTitle> call,
                                               retrofit2.Response<Change_roomTitle> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    // response.body() null만 들어옴.
                                    final Change_roomTitle change_roomTitle = response.body();

                                    room_list.set(get_roomlist_pos, new DataItem2(change_roomTitle.acRoomTitle, change_roomTitle.uiRoomNo));
                                    f2Adapter.notifyDataSetChanged();

//                                    Log.i("tag", title);

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
                        public void onFailure(retrofit2.Call<Change_roomTitle> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete_room() {
        try {
            SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
            RoomNo = sharedPreferences.getString("roomNo", "");

            SharedPreferences sharedPreferences1 = getActivity().getPreferences(MODE_PRIVATE);
            int get_roomlist_pos = sharedPreferences1.getInt("room_list_position", 0);

            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.delete(RoomNo)
                    .enqueue(new retrofit2.Callback<Delete_Room_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Delete_Room_Response> call,
                                               retrofit2.Response<Delete_Room_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    // response.body() null만 들어옴.
                                    final Delete_Room_Response delete_room_response = response.body();

                                    room_list.remove(get_roomlist_pos);
                                    f2Adapter.notifyDataSetChanged();

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
                        public void onFailure(retrofit2.Call<Delete_Room_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.i("tag", "onpause");
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences1 = getActivity().getPreferences(MODE_PRIVATE);
        int get_roomlist_pos = sharedPreferences1.getInt("room_list_position", 0);

        timer = new Timer(true); //인자가 Daemon 설정인데 true 여야 죽지 않음.
        handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable(){
                    public void run(){
//                        Log.i("tag", "onresume");
                    }
                });
            }
        }, 0, 1000);
    }
}