package com.example.indoornavigation.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workshift implements Parcelable {
    @SerializedName("workshift_id")
    @Expose
    private String workshiftId;

    @SerializedName("worker_id")
    @Expose
    private int workerId;

    @SerializedName("start_time")
    @Expose
    private String startTime;

    @SerializedName("end_time")
    @Expose
    private String endTime;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("report_id")
    @Expose
    private String report_id;

    public String getWorkshiftId() {
        return workshiftId;
    }

    public void setWorkshiftId(String workshiftId) {
        this.workshiftId = workshiftId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.workshiftId);
        dest.writeInt(this.workerId);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.status);
        dest.writeString(this.report_id);
    }

    public Workshift(){}

    protected Workshift(Parcel in) {
        this.workshiftId = in.readString();
        //this.workshiftId = (String) in.readValue(UUID.class.getClassLoader());
        this.workerId = in.readInt();
        this.startTime = in.readString();
        //this.startTime = (String) in.readValue(Timestamp.class.getClassLoader());
        //this.endTime = (String) in.readValue(Timestamp.class.getClassLoader());
        this.endTime = in.readString();
        this.status = in.readString();
        //this.report_id = (String) in.readValue(UUID.class.getClassLoader());
        this.report_id = in.readString();
    }

    public static final Parcelable.Creator<Workshift> CREATOR  = new Parcelable.Creator<Workshift>() {
        @Override
        public Workshift createFromParcel(Parcel source) {
            return new Workshift(source);
        }

        @Override
        public Workshift[] newArray(int size) {
            return new Workshift[size];
        }
    };

    @Override
    public String toString() {
        return "Workshift_id: " + this.workshiftId.toString() + "\r\n" +
                "Worker_id: " + this.workerId + "\r\n" +
                "Start time: " + this.startTime.toString() + "\r\n" +
                "End time: " + this.endTime.toString() + "\r\n" +
                "Status: " + this.status + "\r\n" +
                "Report id: " + this.report_id;
    }
}
