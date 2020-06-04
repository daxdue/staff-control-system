package com.example.indoornavigation.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.sql.Timestamp;
import java.util.UUID;

public class Task implements Parcelable {

    private String name;
    private String description;
    private int    executorId;
    private UUID   taskId;
    private int    priority;
    private JsonElement location;
    private TaskLocation taskLocation;
    private String locationName;
    private String status;

    private String workshiftId;



    public Task(String name, String description, int executorId, UUID taskId, int priority, JsonElement location, String status) {
        this.name = name;
        this.description = description;
        this.executorId = executorId;
        this.taskId = taskId;
        this.priority = priority;
        this.location = location;
        this.status = status;
    }

    public Task() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskLocation getLocation() {
        TaskLocation taskLocation = new Gson().fromJson(location, TaskLocation.class);
        return taskLocation;
    }

    public void setLocation(JsonElement location) {
        TaskLocation taskLocation = new Gson().fromJson(location, TaskLocation.class);
        this.taskLocation = taskLocation;
        this.location = location;
    }

    public void setTaskLocation(TaskLocation taskLocation) {
        this.taskLocation = taskLocation;
    }

    public TaskLocation getTaskLocation() {
        return taskLocation;
    }

    public int getExecutorId() {
        return executorId;
    }

    public void setExecutorId(int executorId) {
        this.executorId = executorId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public void createTaskId() {

        this.taskId = UUID.randomUUID();
    }

    public JsonElement getLocationJson() {return this.location;}
    public int getPriority() {
        return priority;
    }

    public String getLocationName() {return taskLocation.getLocationName();}

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkshiftId() {
        return workshiftId;
    }

    public void setWorkshiftId(String workshiftId) {
        this.workshiftId = workshiftId;
    }

    @Override
    public String toString() {
        return "Task name: " + this.getName() + "\r\n" +
                "Task description: " + this.getDescription() + "\r\n" +
                "Executor id: " + this.getExecutorId() + "\r\n" +
                "Task id: " + this.getTaskId().toString() + "\r\n" +
                "Task priority: " + this.getPriority() + "\r\n" +
                "Task location: " + this.getLocationName();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.executorId);
        dest.writeValue(this.taskId);
        dest.writeInt(this.priority);
        dest.writeValue(this.location);
        dest.writeString(this.status);
        dest.writeValue(this.taskLocation);

        dest.writeString(this.workshiftId);

    }

    protected Task(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.executorId = in.readInt();
        this.taskId = (UUID) in.readValue(UUID.class.getClassLoader());
        this.priority = in.readInt();
        this.location = (JsonElement) in.readValue(JsonElement.class.getClassLoader());
        this.status = in.readString();
        this.taskLocation = (TaskLocation) in.readValue(TaskLocation.class.getClassLoader());

        this.workshiftId = in.readString();

    }

    public static final Parcelable.Creator<Task> CREATOR  = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
