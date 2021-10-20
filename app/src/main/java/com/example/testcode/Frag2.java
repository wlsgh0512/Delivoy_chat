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
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.MyAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 채팅 탭 -> 참여중인 채팅 목록 조회.
 * ListView에 데이터 받아와서 .add()하는 식으로 구현하려 했으나 갱신 x.
 * code 200인데 왜 chat_room_response 받아오는 값들이 null ??
 */

public class Frag2 extends Fragment {
    AlertDialog.Builder builder;
    ArrayList<DataItem2> room_list = new ArrayList<>();
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId;

    f2Adapter f2Adapter;

    FragmentFrag2Binding binding;
    ChatroomDialogBinding binding1;

    public Frag2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentFrag2Binding.inflate(getLayoutInflater());
        binding1 = ChatroomDialogBinding.inflate(getLayoutInflater());

        binding.listview2.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager manager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.listview2.setLayoutManager(manager);

        f2Adapter = new f2Adapter(room_list);
        binding.listview2.setAdapter(f2Adapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar", "");
        ucDistribId = sharedPreferences.getString("di", "");
        ucAgencyId = sharedPreferences.getString("ag", "");
        ucMemCourId = sharedPreferences.getString("me", "");

        Chat_room_list();

        f2Adapter.setOnItemClickListener(new f2Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), Chat_friends.class);
                intent.putExtra("uiRoomNo", position + 1);
                startActivity(intent);
                // intent.putExtra로 postion이 아닌 uiroomno를 보내야된다.
            }
        });

        f2Adapter.setOnItemLongClickListener(new f2Adapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                builder = new AlertDialog.Builder(getActivity());

//                builder.setTitle(room_list.get(position));

                builder.setItems(R.array.chat_long, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("채팅방 이름 변경");
                                builder.setView(R.layout.chatroom_dialog);

                                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        return;
                                    }
                                });

                                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        change_title();
                                        Log.i("tag", binding1.addboxdialog.getText().toString());
                                    }
                                });
                                builder.show();
                                break;
                            case 1:
                                // Listview 순서 바꿔주기 후 고정?
                                break;
                            case 2:
                                /**
                                 * 여기서 room_delete.php?
                                 * loom_delete하고 리스트뷰 삭제해주기
                                 */
//                                room_list.remove(i);
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        return binding.getRoot();
    }

    public void showDialog() {
        builder = new AlertDialog.Builder(getActivity());

        // 채팅에 참여중인 명단을 불러와서 settitle?
        builder.setTitle("채팅방");
        builder.setItems(R.array.chat_long, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("채팅방 이름 변경");
                        builder.setView(R.layout.chatroom_dialog);

                        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });

                        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                        break;
                    case 1:
                        // Listview 순서 바꿔주기 후 고정?
                        Toast.makeText(getActivity(), "qwe", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        // 나가기하면 listview 삭제 하면 될듯
                        Toast.makeText(getActivity(), "zxc", Toast.LENGTH_SHORT).show();
                        break;
                }
                dialogInterface.dismiss();
            }
        });
        builder.show();
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

                                    List<Chat_room_Response.Items.Rooms> items = chat_room_response.items.astRooms;
                                    for(int i=0;i<items.size();i++) {
                                        room_list.add(new DataItem2(items.get(i).acRoomTitle));

                                    }


                                    f2Adapter.notifyDataSetChanged();


//                                    SharedPreferences prefs = getActivity().getSharedPreferences(
//                                            "roomno"
//                                            , MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = prefs.edit();
//                                    JSONArray a = new JSONArray();
//                                    for ( int i = 0; i < items.size(); i++) {
//                                        a.put(items.get(i).uiRoomNo);
//                                    }
//                                    editor.commit();

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
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.change("1",
                    binding1.addboxdialog.getText().toString(),
                    ""
//                    uiRoomNo,
//                    acRoomTitle,
//                    acImageUrl
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
//                                    Toast.makeText(getActivity().getApplicationContext(), "Frag2" + chat_room_response.uiRoomNo, Toast.LENGTH_SHORT).show();
                                        builder.setTitle(binding1.addboxdialog.getText().toString());
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



}