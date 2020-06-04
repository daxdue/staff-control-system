package com.example.indoornavigation.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements Parcelable {

    private String name;
    private String position;
    private int tabelId;
    private UUID workerId;
    private ArrayList<Task> tasksList;

    User(String name, String position, int tabelId, UUID workerId) {
        this.name = name;
        this.position = position;
        this.tabelId = tabelId;
        this.workerId = workerId;
    }

    public User() {}

    public void setName(String name) {
        this.name = name;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setTabelId(int tabelId) {
        this.tabelId = tabelId;
    }
    public void setWorkerId(UUID workerId) {
        this.workerId = workerId;
    }
    public void setTasksList(ArrayList<Task> tasksList) {this.tasksList = tasksList;}

    public String getName() {
        return name;
    }
    public String getPosition() {
        return position;
    }
    public int getTabelId() {
        return tabelId;
    }
    public UUID getWorkerId() {
        return workerId;
    }
    public ArrayList<Task> getTasksList() {return  tasksList;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.position);
        dest.writeInt(this.tabelId);
        dest.writeValue(this.workerId);
        //dest.writeTypedList(this.tasksList);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.position = in.readString();
        this.tabelId = in.readInt();
        this.workerId = (UUID) in.readValue(UUID.class.getClassLoader());
        //in.readTypedList(tasksList, Task.CREATOR);
    }

    public static final Parcelable.Creator<User> CREATOR  = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };



}