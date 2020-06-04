package com.example.indoornavigation.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.UUID;

public class Report {
    @SerializedName("report_id")
    @Expose
    private String report_id;

    @SerializedName("worker_id")
    @Expose
    private String worker_id;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("content")
    @Expose
    private String content;

    public Report() {}

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
