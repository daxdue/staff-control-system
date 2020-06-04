package ru.indoornav;

import java.sql.Timestamp;
import java.util.UUID;

public class Workshift {

    private String workshift_id;

    private int worker_id;

    private String start_time;

    private String end_time;

    private String status;

    private String report_id;

    public String getWorkshift_id() {
        return workshift_id;
    }


    public Workshift(String workshift_id, int worker_id, String start_time, String end_time, String status, String report_id) {
        this.workshift_id = workshift_id;
        this.worker_id = worker_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.status = status;
        this.report_id = report_id;
    }

    public Workshift() {};

    public void setWorkshift_id(String workshift_id) {
        this.workshift_id = workshift_id;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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
    public String toString() {
        return "Workshift_id: " + this.workshift_id.toString() + "\r\n" +
                "Worker_id: " + this.worker_id + "\r\n" +
                "Start time: " + this.start_time.toString() + "\r\n" +
                "End time: " + this.end_time.toString() + "\r\n" +
                "Status: " + this.status + "\r\n" +
                "Report id: " + this.report_id;
    }
}

