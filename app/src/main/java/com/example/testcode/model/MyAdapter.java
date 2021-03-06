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

    // ?????? ??? ??? ????????? ???????????? ??????????????? ??????
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
                builder.setMessage("????????? ?????????????????????????")
                        .setCancelable(true)
                        .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                builder.setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteMsg();
                        Toast.makeText(view.getContext(), "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();

                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }

    // ??????????????????????????? ????????? ??? ????????? ??????
    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    // ?????????
    // ?????? 3?????? ?????????????????? ?????? ?????????,
    // ??? ???????????? ViewType????????? ??????????????? ??????(???????????????)

    @Override
    public int getItemViewType(int position) {
        return myDataList.get(position).getViewType();
    }

    // "????????????????????? ????????? ??? ??????", ????????? "??? ??? ????????? ????????? ??????????????? ??????"
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

            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("test", MODE_PRIVATE);    // test ????????? ???????????? ??????, ?????? test key?????? ????????? ?????? ?????? ?????????.
            ucAreaNo = sharedPreferences.getString("ar", "");
            ucDistribId = sharedPreferences.getString("di", "");
            ucAgencyId = sharedPreferences.getString("ag", "");
            ucMemCourId = sharedPreferences.getString("me", "");

            SharedPreferences sharedPreferences2 = view.getContext().getSharedPreferences("gggg",MODE_PRIVATE);    // test ????????? ???????????? ??????, ?????? test key?????? ????????? ?????? ?????? ?????????.
            String RoomNo = sharedPreferences2.getString("inroom", "");

            String xhrvhwltus = String.valueOf(myDataList.get(talkPos).getTalkId());



            LoginService service = (new RetrofitConfig()).getRetrofit().create(LoginService.class);
            service.delete_msg(
                    xhrvhwltus,       // ???????????? ????????? uiTalkNo
                    RoomNo,       // ????????? ?????? ?????? uiRoomNo
                    ucAreaNo,
                    ucDistribId,
                    ucAgencyId,
                    ucMemCourId)
                    .enqueue(new retrofit2.Callback<DeleteMsgResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<DeleteMsgResponse> call,
                                               retrofit2.Response<DeleteMsgResponse> response) {
                            if (response.isSuccessful()) {
                                // ?????? ??????
                                Log.i("tag", "?????? ??????");
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
                                    // ?????? ??????
                                    Log.i("tag", "????????????");
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