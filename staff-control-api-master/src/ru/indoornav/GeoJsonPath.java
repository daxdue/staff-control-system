package ru.indoornav;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ru.indoornav.location.Coordinate;

import java.util.ArrayList;
import java.util.UUID;

public class GeoJsonPath {

    @SerializedName("workshiftId")
    @Expose
    private UUID workshiftId;

    @SerializedName("route")
    @Expose
    private ArrayList<Coordinate> route;

    public GeoJsonPath() {
        route = new ArrayList<Coordinate>();
    }

    public UUID getWorkshiftId() {
        return workshiftId;
    }

    public void setWorkshiftId(UUID workshiftId) {
        this.workshiftId = workshiftId;
    }

    public ArrayList<Coordinate> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<Coordinate> route) {
        this.route = route;
    }

    public void addLocationPoint(Coordinate coordinate) {
        route.add(coordinate);
    }
}
