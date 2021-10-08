package com.example.testcode;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

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

import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
    String hostname = "222.239.254.253";
    static final String[] LIST_MENU = {"김김김", "이이이", "박박박"};
    AlertDialog.Builder builder;
    ArrayList<String> item = new ArrayList<String>();
    EditText addboxdialog;
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId;

    public Frag2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag2, container, false);
        addboxdialog = (EditText) view.findViewById(R.id.addboxdialog);


        ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, item);
        ListView listview2 = (ListView) view.findViewById(R.id.listview2);
        listview2.setAdapter(Adapter);

        item.add("김김김, 나나나");
        item.add("이이이");
        item.add("박박박");
        item.add("최최최");

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        ucAreaNo = sharedPreferences.getString("ar","");
        ucDistribId = sharedPreferences.getString("di","");
        ucAgencyId = sharedPreferences.getString("ag","");
        ucMemCourId = sharedPreferences.getString("me","");

        Chat_room_list();

        // 채팅방을 클릭했을시 채팅창이 열리도록
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Chat_friends.class);
                startActivity(intent);
            }
        });

        // 채팅방을 롱클릭했을시 이름 변경, 상단 고정, 나가기 중 택1
        listview2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                builder = new AlertDialog.Builder(getActivity());
                // 채팅에 참여중인 명단을 불러와서 settitle?
                    builder.setTitle(item.get(pos));

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
                                        /**
                                         * 여기서 room_put.php
                                         */

//                                        Log.d("pjh",addboxdialog.getText().toString());
//                                    int checked;
//                                    int count = Adapter2.getCount();
//                                    if(count > 0) {
//                                        checked = listview2.getCheckedItemPosition();
//                                        if (checked > -1 && checked < count) {
//                                            item.set(checked, Integer.toString(checked+1) + "번 아이템 수정");
//                                            Adapter2.notifyDataSetChanged();
//                                        }
//                                    }

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
                                 */
                                // 나가기하면 listview 삭제 하면 될듯
                                Toast.makeText(getActivity(), "채팅방 나가기", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });

        return view;
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
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/groups/group_fetch.php", hostname);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("ucAreaNo", ucAreaNo);
            urlBuilder.addQueryParameter("ucDistribId", ucDistribId);
            urlBuilder.addQueryParameter("ucAgencyId", ucAgencyId);
            urlBuilder.addQueryParameter("ucMemCourId", ucMemCourId);

//            urlBuilder.addQueryParameter("ucAreaNo", ((TextView) getView().findViewById(R.id.edtUserAreaNo)).getText().toString());
//            urlBuilder.addQueryParameter("ucDistribId", ((TextView) getView().findViewById(R.id.edtUserDistribId)).getText().toString());
//            urlBuilder.addQueryParameter("ucAgencyId", ((TextView) getView().findViewById(R.id.edtUserAgencyId)).getText().toString());
//            urlBuilder.addQueryParameter("ucMemCourId", ((TextView) getView().findViewById(R.id.edtUserCourId)).getText().toString());

            Request request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .get()
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
                        final Chat_room_Response chat_room_response = new Gson().fromJson(responseData, Chat_room_Response.class);
                        getActivity().runOnUiThread(() -> {
                            try {

//                                item.add(chat_room_response.acRoomTitle);

                                /**
                                 * 왜 ListActivity에 진입하는 순간 이 토스트 메시지가 뜨지
                                 */
                                Toast.makeText(getActivity().getApplicationContext(), "Frag2" + chat_room_response.uiRoomNo, Toast.LENGTH_SHORT).show();
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