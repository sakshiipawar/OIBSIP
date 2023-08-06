package com.example.todotask.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotask.AddnewTask;
import com.example.todotask.MainActivity;
import com.example.todotask.R;
import com.example.todotask.Utils.DataBasedHelper;
import com.example.todotask.model.ToDoModel;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> mList;
    private final MainActivity activity;
    private final DataBasedHelper myDB;

    public ToDoAdapter(DataBasedHelper myDB, MainActivity activity) {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.mcheckbox.setText(item.getTask());
        holder.mcheckbox.setChecked(toboolean(item.getStatus()));
        holder.mcheckbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                myDB.updateStatus(item.getId(), 1);
            } else
                myDB.updateStatus(item.getId(), 0);
        });
    }

    public boolean toboolean(int num) {
        return num != 0;
    }

    public Context getContext() {
        return activity;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTask(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task ", item.getTask());

        AddnewTask task = new AddnewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), AddnewTask.TAG);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mcheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mcheckbox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
