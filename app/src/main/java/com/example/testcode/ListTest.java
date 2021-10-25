package com.example.testcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.User_listup_Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListTest extends AppCompatActivity {

    ListView listtest;
    ArrayList<String> listlist = new ArrayList<>();
    private ArrayAdapter Fadapter;
    String testUiRoomNo;
    List<User_listup_Response.Items.AstUser> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_up);

        Fadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listlist) ;

        listtest = (ListView) findViewById(R.id.listtest) ;
        listtest.setAdapter(Fadapter) ;

        testUiRoomNo = getIntent().getStringExtra("RoomNo");

//        listlist.add("asd");
        list_Test();

        listtest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }




    public void list_Test(){
        try {
            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.list_up(
                    testUiRoomNo)
                    .enqueue(new retrofit2.Callback<User_listup_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<User_listup_Response> call,
                                               retrofit2.Response<User_listup_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final User_listup_Response user_listup_response = response.body();
                                    items = user_listup_response.items.astUsers;

                                    for (int i = 0; i < items.size(); i++) {
                                        listlist.add(items.get(i).acRealName);
                                    }
                                    Fadapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                final ErrorDto error;
                                try {
                                    error = new Gson().fromJson(response.errorBody().string(),
                                            ErrorDto.class);
                                    Log.i("tag", "" + error.message);
                                    // 응답 실패
                                    Log.i("tag", "응답실패");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<User_listup_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}