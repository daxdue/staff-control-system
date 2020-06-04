package ru.indoornav.location;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Location {

    @SerializedName("workerUUID")
    @Expose
    private UUID workshiftUUID;

    @SerializedName("locationData")
    @Expose
    private String locationData;

    public Location(UUID workshiftUUID, LocationData locationData) {
        this.workshiftUUID = workshiftUUID;
        this.locationData = new Gson().toJson(locationData);
    }

    public UUID getWorkshiftUUID() {
        return workshiftUUID;
    }

    public void setWorkshiftUUID(UUID workshiftUUID) {
        this.workshiftUUID = workshiftUUID;
    }

    public String getLocationData() {
        return locationData;
    }

    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }
}

