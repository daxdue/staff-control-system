package com.example.indoornavigation;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Task> tasks;

    TasksAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.tasks_list_adapter, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TasksAdapter.ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.nameOfTask.setText(task.getNameOfTask());
        holder.taskDescription.setText(task.getTaskDescription());
        holder.taskLocation.setText(task.getTaskLocation());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameOfTask, taskDescription, taskLocation;
        ViewHolder(View view){
            super(view);
            nameOfTask = (TextView) view.findViewById(R.id.nameOfTask);
            taskDescription = (TextView) view.findViewById(R.id.taskDescription);
            taskLocation = (TextView) view.findViewById(R.id.taskLocation);
        }
    }
}
