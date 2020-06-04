package com.example.indoornavigation;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import java.util.List;

import com.example.indoornavigation.data.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Task> tasks;

    TaskAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.tasks_list_adapter, parent, false);
        return new TaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.nameOfTask.setText(task.getName());
        holder.taskDescription.setText(task.getDescription());
        holder.taskLocation.setText("Местоположение: " + task.getLocationName());
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

    public void updateTasksList(List<Task> newTaskList) {
        tasks.clear();
        tasks.addAll(newTaskList);
        this.notifyDataSetChanged();
    }
}
