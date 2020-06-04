package com.example.indoornavigation.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;

import java.util.UUID;

public class TaskLocation implements Parcelable {

    private String locationName;
    private String roomId;
    private String latitude;
    private String longitude;

    public TaskLocation(String locationName, String roomId, String latitude, String longitude) {
        this.locationName = locationName;
        this.roomId = roomId;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locationName);
        dest.writeString(this.roomId);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);


    }

    protected TaskLocation(Parcel in) {
        this.locationName = in.readString();
        this.roomId = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
    }

    public static final Parcelable.Creator<TaskLocation> CREATOR  = new Parcelable.Creator<TaskLocation>() {
        @Override
        public TaskLocation createFromParcel(Parcel source) {
            return new TaskLocation(source);
        }

        @Override
        public TaskLocation[] newArray(int size) {
            return new TaskLocation[size];
        }
    };


}
