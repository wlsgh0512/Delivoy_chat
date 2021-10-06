package com.example.testcode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.google.gson.Gson;

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
 * addQueryParameter 부분이 의심..
 */

public class Frag1 extends Fragment {
    String hostname = "222.239.254.253";
    static final String[] LIST_MENU = {"가가가", "나나나", "다다다", "라라라", "마마마", "바바바", "사사사",};
    ArrayList<String> friends_name = new ArrayList<String>();
//    ArrayList<String> friends_name;
    private ArrayAdapter Fadapter;
    private ListView listview;
    ListActivity listActivity;
    View view;

    //    public static Context context_frag1;
    public Frag1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag1, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listview = (ListView) view.findViewById(R.id.listview1);
        Fadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, friends_name);
        listview.setAdapter(Fadapter);

        // listview를 클릭했을 때 채팅방으로 넘어가도록,,
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> apterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), Chat.class);
                startActivity(intent);
            }
        });
//        test();
        friends_name.add("asd");
        Friends_list();

//        Toast.makeText(getActivity().getApplicationContext(), "응답 : ", Toast.LENGTH_SHORT).show();
//        Fadapter.notifyDataSetChanged();

    }




    public void Friends_list() {

        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("http://%s/chatt/app/friends/friend_fetch.php", hostname);

            TextView edtUserAreaNo = view.findViewById(R.id.edtUserAreaNo);

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("ucAreaNo", ((TextView) view.findViewById(R.id.edtUserAreaNo)).getText().toString());
            urlBuilder.addQueryParameter("ucDistribId", ((TextView) view.findViewById(R.id.edtUserDistribId)).getText().toString());
            urlBuilder.addQueryParameter("ucAgencyId", ((TextView) view.findViewById(R.id.edtUserAgencyId)).getText().toString());
            urlBuilder.addQueryParameter("ucMemCourId", ((TextView) view.findViewById(R.id.edtUserCourId)).getText().toString());

            Request request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "응답2", Toast.LENGTH_SHORT).show();
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
                        final FriendsResponse friendsResponse = new Gson().fromJson(responseData, FriendsResponse.class);
                        final FriendsResponse.Items items = new Gson().fromJson(responseData, FriendsResponse.Items.class);
                        final FriendsResponse.Root Root = new Gson().fromJson(responseData, FriendsResponse.Root.class);

                        getActivity().runOnUiThread(() -> {
                            try {

                                friends_name.add(friendsResponse.acRealName);

                                Log.i("tag", friendsResponse.acRealName);

//                                Fadapter.notifyDataSetChanged();

                                Toast.makeText(getActivity().getApplicationContext(), "응답 : " + friendsResponse.acRealName, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } // end of else
                } // end of onResponse
            });   // end of callback
        } catch (Exception e) {
            e.printStackTrace();
        }
    }             // end of method

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



