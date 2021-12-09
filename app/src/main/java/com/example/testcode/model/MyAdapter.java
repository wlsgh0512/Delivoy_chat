package com.example.testcode.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testcode.R;
import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DataItem> myDataList;
    View view;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
    String getTime = dateFormat.format(date);

    public MyAdapter(ArrayList<DataItem> dataList) {
        myDataList = dataList;
    }

    String ucAreaNo, ucDistribId, ucAgencyId, ucMemCourId, testUiRoomNo;

    public DataItem getLastItem() {
        return myDataList.get(myDataList.size() - 1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == Code.ViewType.CENTER_CONTENT) {
            view = inflater.inflate(R.layout.center, parent, false);
            return new CenterViewHolder(view);
        } else if (viewType == Code.ViewType.LEFT_CONTENT) {
            view = inflater.inflate(R.layout.left, parent, false);
            return new LeftViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.right, parent, false);
            return new RightViewHolder(view);
        }

    }

    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CenterViewHolder) {
            ((CenterViewHolder) viewHolder).textv.setText(myDataList.get(position).getContent());
        } else if (viewHolder instanceof LeftViewHolder) {
            ((LeftViewHolder) viewHolder).textv_nicname.setText(myDataList.get(position).getName());
            ((LeftViewHolder) viewHolder).textv_msg.setText(myDataList.get(position).getContent());
            ((LeftViewHolder) viewHolder).textv_time.setText(myDataList.get(position).getTime());
        } else {
            ((RightViewHolder) viewHolder).textv_msg.setText(myDataList.get(position).getContent());
            ((RightViewHolder) viewHolder).textv_time.setText(myDataList.get(position).getTime());
        }

        (viewHolder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                SharedPreferences spf = view.getContext().getSharedPreferences("mmmm", MODE_PRIVATE);
                SharedPreferences.Editor edi = spf.edit();
                edi.putInt("talkpos", position);
                edi.putInt("lr", myDataList.get(position).getViewType());
                edi.commit();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("채팅을 삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteMsg();
                        Toast.makeText(view.getContext(), "채팅이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }

    // 리사이클러뷰안에서 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    // ★★★
    // 위에 3개만 오버라이드가 기본 셋팅임,
    // 이 메소드는 ViewType때문에 오버라이딩 했음(구별할려고)

    @Override
    public int getItemViewType(int position) {
        return myDataList.get(position).getViewType();
    }

    // "리사이클러뷰에 들어갈 뷰 홀더", 그리고 "그 뷰 홀더에 들어갈 아이템들을 셋팅"
    public class CenterViewHolder extends RecyclerView.ViewHolder {
        TextView textv;

        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            textv = (TextView) itemView.findViewById(R.id.textv);
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView textv_nicname;
        TextView textv_msg;
        TextView textv_time;
        TextView ucConfirmFlag;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            textv_nicname = (TextView) itemView.findViewById(R.id.textv_nicname);
            textv_msg = (TextView) itemView.findViewById(R.id.textv_msg);
            textv_time = (TextView) itemView.findViewById(R.id.textv_time);
            ucConfirmFlag = (TextView) itemView.findViewById(R.id.ucConfirmFlag);

        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {
        TextView textv_msg;
        TextView textv_time;
        TextView ucConfirmFlag;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            textv_msg = (TextView) itemView.findViewById(R.id.textv_msg);
            textv_time = (TextView) itemView.findViewById(R.id.textv_time);
            ucConfirmFlag = (TextView) itemView.findViewById(R.id.ucConfirmFlag);
        }
    }

    public void deleteMsg(){
        try {

            SharedPreferences sharedPreferences1 = view.getContext().getSharedPreferences("mmmm", MODE_PRIVATE);
            int talkPos = sharedPreferences1.getInt("talkpos", 0);
            int vtype = sharedPreferences1.getInt("lr", 0);

            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            ucAreaNo = sharedPreferences.getString("ar", "");
            ucDistribId = sharedPreferences.getString("di", "");
            ucAgencyId = sharedPreferences.getString("ag", "");
            ucMemCourId = sharedPreferences.getString("me", "");

            SharedPreferences sharedPreferences2 = view.getContext().getSharedPreferences("gggg",MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            String RoomNo = sharedPreferences2.getString("inroom", "");

            String xhrvhwltus = String.valueOf(myDataList.get(talkPos).getTalkId());



            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.delete_msg(
                    xhrvhwltus,       // 롱클릭한 채팅의 uiTalkNo
                    RoomNo,       // 진입해 있는 방의 uiRoomNo
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId)
                    .enqueue(new retrofit2.Callback<DeleteMsgResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<DeleteMsgResponse> call,
                                               retrofit2.Response<DeleteMsgResponse> response) {
                            if (response.isSuccessful()) {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                try {
                                    final DeleteMsgResponse deleteMsgResponse = response.body();
                                    myDataList.set(talkPos, new DataItem(talkPos, "The message has been deleted", "",
                                            vtype, getTime));
//                                    notifyDataSetChanged();
                                    notifyItemChanged(talkPos);

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
                        public void onFailure(retrofit2.Call<DeleteMsgResponse> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}