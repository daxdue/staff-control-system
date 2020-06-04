package ru.indoornav.rest;

import com.google.gson.JsonElement;

public class StandartResponse {

    private StatusResponse status;
    private String message;
    private String dataType;
    private JsonElement data;

    public StandartResponse(StatusResponse status) {
        this.status = status;
    }

    public StandartResponse(StatusResponse status, String message) {
        this.status = status;
        this.message = message;
    }

    public StandartResponse(StatusResponse status, JsonElement data) {
        this.status = status;
        this.data = data;
    }

    public StandartResponse(StatusResponse status, String dataType, JsonElement data) {
        this.status = status;
        this.dataType = dataType;
        this.data = data;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
