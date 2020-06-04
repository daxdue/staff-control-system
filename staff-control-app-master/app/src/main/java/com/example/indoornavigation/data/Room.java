package com.example.indoornavigation.data;

public class Room {

    private String roomName;
    private String roomId;
    private String latitude;
    private String longitude;

    public Room(String roomName, String roomId, String latitude, String longitude) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
