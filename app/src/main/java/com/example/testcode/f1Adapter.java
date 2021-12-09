package com.example.testcode;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testcode.api.LoginService;
import com.example.testcode.components.FriendList;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.F1FriendsListBinding;
import com.example.testcode.databinding.FragmentFrag1Binding;
import com.example.testcode.databinding.FriendListBinding;
import com.example.testcode.model.Add_Friends_Response;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.MyAdapter;
import com.example.testcode.model.NonFriendsAdapter;
import com.example.testcode.model.NonFriendsData;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class f1Adapter extends RecyclerView.Adapter{

    private ArrayList<DataItem2> f1List;
    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId,
           ucFriAreaNo,ucFriDistribId,ucFriAgencyId,ucFriMemCourId;
    Context context;
    View view;

    private MyApplication application;

    private Set<DataItem2> mAll = new HashSet<>();

    public f1Adapter(
            ArrayList<DataItem2> friends_name) {
        f1List = friends_name;

    }

    private OnItemClickListener mListener = null ;
    private OnItemLongClickListener mLongListener = null ;

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position) ;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongListener = listener ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.f1_friends_list, parent, false);
        application = (MyApplication) context.getApplicationContext();

        return new f1Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DataItem2 data = f1List.get(position);
        ((f1Holder) holder).friends.setText(data.getContent());

        if ( application.getCurrentActivity() instanceof ListActivity) {
            ((f1Holder) holder).friends.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), Chat_friends.class);
                intent.putExtra("uiRoomNo", data.getRno());
                view.getContext().startActivity(intent);
            });

            ((f1Holder) holder).friends.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    SharedPreferences spf = view.getContext().getSharedPreferences("cour", MODE_PRIVATE);
                    SharedPreferences.Editor edi = spf.edit();
                    edi.putString("FriMemCour", data.getMemCour());
                    edi.commit();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage(data.getContent() + "를 친구 목록에서 삭제하시겠습니까?")
                            .setCancelable(true)
                            .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    return;
                                }
                            });
                    builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences spf = view.getContext().getSharedPreferences("delete_pos", MODE_PRIVATE);
                            SharedPreferences.Editor edi = spf.edit();
                            edi.putInt("fposition", position);
                            edi.commit();

                            DeleteFriends();
                            return;
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
            });
        }
//        if ( application.getCurrentActivity() instanceof Chat_invite_room ) {
//            (holder.itemView).setBackgroundColor(mAll.contains(data) ? Color.LTGRAY : Color.WHITE);
//
//            ((f1Holder) holder).friends.setOnClickListener(view ->
//            {
//                if ( mAll.contains(data))
//                    mAll.remove(data);
//                else
//                    mAll.add(data);
//
//                this.notifyDataSetChanged();
//        });
//    }
    }


    @Override
    public int getItemCount() {
        return f1List.size();
    }

    public class f1Holder extends RecyclerView.ViewHolder {
        TextView friends;

        public f1Holder(@NonNull View itemView) {
            super(itemView);
            friends = (TextView) itemView.findViewById(R.id.friends);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(view,pos);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        if(mLongListener !=null){
                            mLongListener.onItemLongClick(view,pos);
                        }
                    }
                    return true;
                }
            });

        }
    }

    public void DeleteFriends() {
        try {
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            ucAreaNo = sharedPreferences.getString("ar", "");
            ucDistribId = sharedPreferences.getString("di", "");
            ucAgencyId = sharedPreferences.getString("ag", "");
            ucMemCourId = sharedPreferences.getString("me", "");

            SharedPreferences sharedPreferences0 = view.getContext().getSharedPreferences("cour", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            ucFriMemCourId = sharedPreferences0.getString("FriMemCour", "");

            SharedPreferences sharedPreferences1 = view.getContext().getSharedPreferences("zxcv", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            ucFriAreaNo = sharedPreferences1.getString("aa" , "");
            ucFriDistribId = sharedPreferences1.getString("bb" , "");
            ucFriAgencyId = sharedPreferences1.getString("cc" , "");


            SharedPreferences sharedPreferences2 = view.getContext().getSharedPreferences("delete_pos", MODE_PRIVATE);
            int getFpos = sharedPreferences2.getInt("fposition", 0);


            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.deletefriends(ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId,
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucFriMemCourId)
                    .enqueue(new retrofit2.Callback<Add_Friends_Response>() {
                        @Override
                        public void onResponse(retrofit2.Call<Add_Friends_Response> call,
                                               retrofit2.Response<Add_Friends_Response> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final Add_Friends_Response add_friends_response = response.body();

                                    f1List.remove(getFpos);
                                    notifyDataSetChanged();
                                    Toast.makeText(view.getContext(), "친구목록에서 삭제했습니다.", Toast.LENGTH_SHORT).show();

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
                        public void onFailure(retrofit2.Call<Add_Friends_Response> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
