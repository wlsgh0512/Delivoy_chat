package com.example.testcode;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class f2Adapter extends RecyclerView.Adapter{

    private ArrayList<DataItem2> f2List;
    private Frag2 f2;

    public f2Adapter(ArrayList<DataItem2> room_list, Frag2 f2) {
        f2List = room_list;
        this.f2 = f2;
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
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.f2_room_list, parent, false);
        return new f2Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((f2Adapter.f2Holder) holder).room.setText(f2List.get(position).getContent());

        ((f2Holder) holder).room.setOnClickListener(view -> {
            Intent intent = new Intent(f2.getActivity(), Chat_friends.class);
            intent.putExtra("uiRoomNo", f2.items.get(position).uiRoomNo);
            f2.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return f2List.size();
    }

    public class f2Holder extends RecyclerView.ViewHolder {
        TextView room;

        public f2Holder(@NonNull View itemView) {
            super(itemView);
            room = (TextView) itemView.findViewById(R.id.room);

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
}
