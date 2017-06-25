package com.example.pappu_pc.testchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pappu_pc.testchat.ChatRooms;
import com.example.pappu_pc.testchat.MainActivity;
import com.example.pappu_pc.testchat.Messages;
import com.example.pappu_pc.testchat.R;
import com.example.pappu_pc.testchat.model.ChatRoom;
import com.example.pappu_pc.testchat.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pappu-pc on 6/11/2017.
 */

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.MyViewHolder> {
   private ArrayList<User> postLists;
    private Context context;
    public ChatRoomsAdapter(ArrayList<User> postLists, Context context) {
        this.postLists = postLists;
        this.context = context;
    }

    @Override
    public  ChatRoomsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view=LayoutInflater.from(context).inflate(R.layout.chat_rooms_list_row,parent,false);
        MyViewHolder holder=new MyViewHolder(view,context,postLists);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User userModel=postLists.get(position);
        holder.name.setText(userModel.getName());



    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        ArrayList<User> postLists=new ArrayList<>();
        Context context;
        public MyViewHolder(View itemView,Context context, ArrayList<User> postLists) {
            super(itemView);
            this.context=context;
            this.postLists=postLists;
            name= (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            User userModel=this.postLists.get(position);
            Intent intent=new Intent(this.context,Messages.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("chat_email",userModel.getEmail());
            intent.putExtra("chat_token",userModel.getToken());
            context.startActivity(intent);

        }
    }
}

