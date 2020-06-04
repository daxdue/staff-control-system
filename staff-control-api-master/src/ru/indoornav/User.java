package ru.indoornav;

import java.util.ArrayList;
import java.util.UUID;

public class User {

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


}