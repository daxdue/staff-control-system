package com.example.indoornavigation.data;

public enum StatusResponse {
    SUCCESS("Success"), ERROR("Error"), OK("OK"), SERVER_ERROR("Server Error");

    final private String status;

    StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
