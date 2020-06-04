package ru.indoornav;

import java.sql.Timestamp;
import java.util.UUID;

public class Report {

    private String report_id;
    private String worker_id;
    private String time;
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

    @Override
    public String toString() {
        return "Report ID: " + report_id + "\r\n" +
                "Worker ID: " + worker_id + "\r\n" +
                "Time: " + time + "\r\n" +
                "Content: " + content;
    }
}
