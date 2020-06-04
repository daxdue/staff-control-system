package com.example.indoornavigation.data;

import com.example.indoornavigation.location.Coordinate;
import com.example.indoornavigation.location.GeoJsonPath;
import com.example.indoornavigation.location.LocationData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.UUID;

public class LocationService {

    private static LocationService locationService;
    private static LocationData locationData;
    private static GeoJsonPath geoJsonPath;
    private static UUID workshiftUUID;

    private LocationService() {
        //locationData = new LocationData();
        geoJsonPath = new GeoJsonPath();
    }

    public static LocationService getInstance() {
        if(locationService == null) {
            locationService = new LocationService();
        }
        return locationService;
    }


    public GeoJsonPath getLocationData() {
        return geoJsonPath;
    }

    public static void setWorkshiftUUID(UUID workshiftId) {
        workshiftUUID = workshiftId;
    }



    public ApiResponse getLocationJsonData() {

        geoJsonPath.setWorkshiftId(workshiftUUID);
        JsonElement routeData = new Gson().toJsonTree(geoJsonPath, GeoJsonPath.class);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus("Success");
        apiResponse.setDataType("location");
        apiResponse.setData(routeData);
        return apiResponse;
    }


}
