package com.example.testcode.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testcode.AddFriends;
import com.example.testcode.R;
import com.example.testcode.f1Adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NonFriendsAdapter extends RecyclerView.Adapter {

    private ArrayList<NonFriendsData> allList;
    View view;
    Context context;

    AddFriends addFriends;

    private Set<NonFriendsData> mAll = new HashSet<>();

    public NonFriendsAdapter(ArrayList<NonFriendsData> all, AddFriends addFriends) {
        allList = all;
        this.addFriends = addFriends;
    }

    private f1Adapter.OnItemClickListener mListener = null ;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.nonfriends_list, parent, false);

        return new AllHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NonFriendsData data = allList.get(position);
        ((AllHolder)holder).allusers.setText(data.getName());

        (holder.itemView).setBackgroundColor(mAll.contains(data) ? Color.LTGRAY : Color.WHITE);

        ((AllHolder)holder).allusers.setOnClickListener(view ->
        {
            if ( mAll.contains(data))
                mAll.remove(data);
            else
                mAll.add(data);

            this.notifyDataSetChanged();


            String selectUser = data.getName();
                addFriends.addUserSetText(selectUser);

                // 친구 추가에 사용
                SharedPreferences spf = view.getContext().getSharedPreferences("pppp", MODE_PRIVATE);
                SharedPreferences.Editor edi = spf.edit();
                edi.putString("selectUser", data.getName());
                edi.putString("FriMemCour", data.getMemCour());
                edi.commit();


        });
    }

    @Override
    public int getItemCount() {
        return allList.size();
    }

    public class AllHolder extends RecyclerView.ViewHolder {
        TextView allusers;
        LinearLayout wrappera;

        public AllHolder(@NonNull View itemView) {
            super(itemView);
            wrappera = (LinearLayout) itemView.findViewById(R.id.wrappera);
            allusers = (TextView) itemView.findViewById(R.id.allusers);

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
        }
    }
}
