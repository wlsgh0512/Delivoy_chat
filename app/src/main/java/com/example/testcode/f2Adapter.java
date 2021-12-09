package com.example.testcode;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Person;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testcode.api.LoginService;
import com.example.testcode.config.RetrofitConfig;
import com.example.testcode.databinding.F2RoomListBinding;
import com.example.testcode.databinding.FragmentFrag2Binding;
import com.example.testcode.databinding.TestRoomlistBinding;
import com.example.testcode.model.Change_roomTitle;
import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.DataItem;
import com.example.testcode.model.DataItem2;
import com.example.testcode.model.Delete_Room_Response;
import com.example.testcode.model.ErrorDto;
import com.example.testcode.model.MyAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView의 아이템 꼬임 ? 섞임 ? 발생
 * getitemviewtype 수정 , setTag 주석 후 수정된듯 하다. 추가로 찾아보기
 *
 */

public class f2Adapter extends RecyclerView.Adapter{

    private ArrayList<DataItem2> f2List;
    AlertDialog.Builder builder;
    EditText editText;
    String RoomNo;
    View view;

    private F2RoomListBinding binding;
    Context context;
    private MyApplication application;

    public DataItem2 getLastItem() {
        return f2List.get(f2List.size() - 1);
    }

    public f2Adapter(ArrayList<DataItem2> room_list) {
        f2List = room_list;
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

    public void Clear() {
        f2List.clear();
    }

    //    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.f2_room_list, parent, false);
        application = (MyApplication) context.getApplicationContext();

        return new f2Holder(view);
    }

    /**
     * onBindViewHolder 메소드 안에 final int position을 사용하지 말라. (final인 position은 보장하지 않는다. 위치가 바뀌거나 지워질때 등)
     * -> 해결방법. holder.getAdapterPosition을 이용해라.
     * -> 2020년 2월 부터는 getAbsoluteAdapterPosition을 사용하도록 권고
     * 3) holder.getAdapterPosition()해서 가져온 position값이 RecyclerView.NO_POSITION인지 꼭 확인하도록 해라.
     * -> 방법. if(holder.getAdapterPosition()!=RecyclerView.NO_POSITION)인지 확인하고 필요한 로직 추가.
     * 나중에 확인해보기
     */

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((f2Adapter.f2Holder) holder).room.setText(f2List.get(position).getContent());
//        ((f2Holder) holder).room.setTag(position);


        if ( application.getCurrentActivity() instanceof ListActivity) {
            ((f2Holder) holder).room.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), Chat_friends.class);
                intent.putExtra("uiRoomNo", f2List.get(position).getRno());
                intent.putExtra("acRoomTitle", f2List.get(position).getContent());
//            intent.putExtra("uiTalkNo", f2List.get(position))
                view.getContext().startActivity(intent);

                SharedPreferences ss = view.getContext().getSharedPreferences("gggg", MODE_PRIVATE);
                SharedPreferences.Editor ed = ss.edit();
                ed.putString("inroom", f2List.get(position).getRno());
                ed.commit();

            }); // end of OnClick

            ((f2Holder) holder).room.setOnLongClickListener(view -> {
                SharedPreferences ss = view.getContext().getSharedPreferences("llll", MODE_PRIVATE);
                SharedPreferences.Editor ed = ss.edit();
                ed.putString("mnm", f2List.get(position).getRno());
                ed.commit();

                SharedPreferences spf = view.getContext().getSharedPreferences("aaaa", MODE_PRIVATE);
                SharedPreferences.Editor edi = spf.edit();
                edi.putInt("room_list_position", position);
                edi.commit();

                builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle(f2List.get(position).getContent());

                builder.setItems(R.array.chat_long, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("채팅방 이름 변경");
                                editText = new EditText(view.getContext());
                                builder.setView(editText);

                                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        return;
                                    }
                                });

                                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        change_title();
                                        Toast.makeText(view.getContext().getApplicationContext(), "채팅방 이름이 변경되었습니다", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(view.getContext().getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.show();
                                break;
                            case 1:
//                                f2List.set(get_roomlist_pos, new DataItem2(change_roomTitle.acRoomTitle, change_roomTitle.uiRoomNo,""));
//                            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(
//                                    "bbbb"
//                                    , MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("ar", f2List.get(position).getRno());
////                            editor.putString("rr", items.get(position).uiRoomNo);
//                            editor.commit();

                                // Listview 순서 바꿔주기 후 고정?
                                break;
                            case 2:
//                            SharedPreferences spf = view.getContext().getSharedPreferences("aaaa", MODE_PRIVATE);
//                                SharedPreferences.Editor edi = spf.edit();
//                                edi.putInt("room_list_position", position);
//                                edi.commit();

                                delete_room();
                                Toast.makeText(view.getContext().getApplicationContext(), "채팅방이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                break;
                        } // end of switch
                        dialogInterface.dismiss();
                    } // end of onclick
                });
                builder.show();
                return false;
            }); // end of long
        } else {
            Log.d("test", "check " + position + application.getCurrentActivity().getClass().getName());
        }
    } // end of onBindViewHolder

    @Override
    public int getItemCount() {
        return f2List.size();
    }

    public class f2Holder extends RecyclerView.ViewHolder {
        TextView room;
        LinearLayout wrapperf;

        public f2Holder(@NonNull View itemView) {
            super(itemView);
            wrapperf = (LinearLayout) itemView.findViewById(R.id.wrapperf);
            room = (TextView) itemView.findViewById(R.id.room);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 // 현재 자신의 위치를 확인할 수 있는 메서드.
                 // 내 아이템의 위치, NO_POSITION인지에 대한 검사 필요.
                    int pos = getAdapterPosition();

                 // notifyDataSetChanged()에 의해 리사이클러뷰가 아이템뷰를 갱신하는 과정에서,
                 // 뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 getAdapterPosition() 메서드는 NO_POSITION을 리턴하기 때문.
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

    public void change_title() {
        try {
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("llll",MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            RoomNo = sharedPreferences.getString("mnm", "");

            SharedPreferences sharedPreferences1 = view.getContext().getSharedPreferences("aaaa", MODE_PRIVATE);
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
                                    final Change_roomTitle change_roomTitle = response.body();

                                    f2List.set(get_roomlist_pos, new DataItem2(change_roomTitle.acRoomTitle, change_roomTitle.uiRoomNo,""));
                                    notifyItemChanged(get_roomlist_pos);

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
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("llll", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            RoomNo = sharedPreferences.getString("mnm", "");

            SharedPreferences sharedPreferences1 = view.getContext().getSharedPreferences("aaaa", MODE_PRIVATE);
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
                                    final Delete_Room_Response delete_room_response = response.body();

                                    f2List.remove(get_roomlist_pos);
                                    notifyDataSetChanged();
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

}
