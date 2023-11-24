package com.example.medmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medmanager.mydatabase.MedicalDB;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter {
    private Cursor user_list;
    public Context context;
    public MedicalDB helper;

    public UserListAdapter(Context context, MedicalDB helper) {
        this.context = context;
        this.helper = helper;
    }

    public void setUserData(Cursor cursor){
        this.user_list = cursor;
        if(user_list!=null)
        {
            user_list.moveToFirst();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (user_list != null && user_list.moveToPosition(position)) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.tv.setText(user_list.getString(1));
            viewHolder.id = user_list.getInt(0);

            viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.deleteUser(helper.getWritableDatabase(), String.valueOf(viewHolder.id));
                    user_list = helper.getUserList(helper.getWritableDatabase());
                    setUserData(user_list);
                    notifyDataSetChanged();
                    if (user_list.getCount() == 0) {
                        MainActivity.empty_view.setText(R.string.empty_users);
                    }
                }
            });

            viewHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MedicineActivity.class);
                    intent.putExtra("userId", ((MyViewHolder) holder).id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this line
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Log.e("Intent error", "onClick: Error", e);
                    }
                }

            });

            // It's generally a good practice to close the cursor when you're done with it.
            // Assuming you have already closed the cursor elsewhere, you may want to check its state before using it.
            // user_list.close();
        }
    }


    @Override
    public int getItemCount() {
        return user_list.getCount();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageButton deleteBtn;
        int id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.user_name);
            deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteUser);
        }
    }

}
