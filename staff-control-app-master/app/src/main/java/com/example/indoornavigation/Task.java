package com.example.indoornavigation;

public class Task {

    private String nameOfTask;
    private String taskDescription;
    private String taskLocation;

    public Task(String nameOfTask, String taskDescription, String taskLocation) {
        this.nameOfTask = nameOfTask;
        this.taskDescription = taskDescription;
        this.taskLocation = taskLocation;
    }

    public String getNameOfTask() {return this.nameOfTask;}
    public void setNameOfTask(String nameOfTask) {this.nameOfTask = nameOfTask;}
    public String getTaskDescription() {return this.taskDescription;}
    public void  setTaskDescription(String taskDescription) {this.taskDescription = taskDescription;}
    public String getTaskLocation() {return this.taskLocation;}
    public void setTaskLocation(String taskLocation) {this.taskLocation = taskLocation;}


}
