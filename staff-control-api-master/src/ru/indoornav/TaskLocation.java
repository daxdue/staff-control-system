package ru.indoornav;

public class TaskLocation {

    private String locationName;
    private String roomId;
    private String latitude;
    private String longitude;

    public TaskLocation(String locationName, String roomId, String latitude, String longitude) {
        this.locationName = locationName;
        this.roomId = roomId;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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
