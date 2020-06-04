package ru.indoornav.location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationData {

    @SerializedName("workshift_id")
    @Expose
    private UUID workshiftId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;

    private static final String FEATURE_TYPE = "Feature";
    private static final String GEOMETRY_TYPE = "Point";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public UUID getWorkshiftId() {
        return workshiftId;
    }

    public void setWorkshiftId(UUID workshiftId) {
        this.workshiftId = workshiftId;
    }

    public void addLocationPoint(Coordinate coordinate) {
        List<Double> coordinates = new ArrayList<Double>();
        coordinates.add(coordinate.getLongitude());
        coordinates.add(coordinate.getLatitude());
        Feature feature = new Feature();
        feature.setType(FEATURE_TYPE);
        feature.setGeometry(new Geometry(GEOMETRY_TYPE, coordinates));
        features.add(feature);
    }

}
