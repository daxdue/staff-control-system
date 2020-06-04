package ru.indoornav;

import com.google.gson.Gson;

import java.util.UUID;

public class Task {

    private String name;
    private String description;
    private String location;
    private int    executorId;
    private UUID   taskId;
    private int    priority;
    private TaskLocation taskLocation;
    private String status;

    private String updated;
    private String workshiftId;



    public Task(String name, String description, String location, int executorId, UUID taskId, int priority, String status) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.executorId = executorId;
        this.taskId = taskId;
        this.priority = priority;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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

    public int getPriority() {
        return priority;
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

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getWorkshiftId() {
        return workshiftId;
    }

    public void setWorkshiftId(String workshiftId) {
        this.workshiftId = workshiftId;
    }
}
