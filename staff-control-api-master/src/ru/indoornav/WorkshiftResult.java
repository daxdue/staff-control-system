package ru.indoornav;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkshiftResult {

    private String report;
    private String workshift;


    WorkshiftResult() {}

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