package com.example.indoornavigation.data;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkshiftResult {
    @SerializedName("workshift")
    @Expose
    private String workshift;

    @SerializedName("report")
    @Expose
    private String report;

    public String getWorkshift() {
        return workshift;
    }

    public void setWorkshift(String workshift) {
        this.workshift = workshift;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
