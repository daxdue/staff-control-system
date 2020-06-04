package ru.indoornav;

public class Room {

    private int roomId;
    private String roomName;
    private String latitude;
    private String longitude;


    public Room(int roomId, String roomName, String latitude, String longitude) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Room() {}
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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
