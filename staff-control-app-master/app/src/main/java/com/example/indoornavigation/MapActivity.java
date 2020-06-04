package com.example.indoornavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoornavigation.data.ApiResponse;
import com.example.indoornavigation.data.Location;
import com.example.indoornavigation.data.LocationService;
import com.example.indoornavigation.data.NetworkService;
import com.example.indoornavigation.data.Room;
import com.example.indoornavigation.data.Task;
import com.example.indoornavigation.data.TaskLocation;
import com.example.indoornavigation.data.User;
import com.example.indoornavigation.data.Workshift;
import com.example.indoornavigation.location.Coordinate;
import com.example.indoornavigation.location.LocationData;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.CoordinateContainer;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfJoins;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * Display an indoor map of a building with toggles to switch between floor levels
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, BeaconConsumer {

    private GeoJsonSource indoorBuildingSource;

    private List<List<Point>> boundingBoxList;
    private View levelButtons;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private TextView textView;
    private TextView taskNameText;
    private TextView taskDescriptionText;
    private TextView taskLocationText;
    private Button menuButton;
    private Button completeTask;
    private Button homeButton;
    private SymbolManager symbolManager;
    private List<Symbol> symbols = new ArrayList<>();

    private BeaconManager beaconManager;
    MarkerViewManager markerViewManager;
    private double distances[];

    private double iPhoneLatitude = 60.0578220897725;
    private double iPhoneLongitude = 30.420949459075928;

    private double macbookLatitude = 60.05780200837197;
    private double macbookLongitude = 30.420895814895626;

    private static final int REQUEST_CODE = 5678;

    private double delta1_3 = 4.5;/*0.00000669379;*/ //стол кух диван
    private double delta1_2 = 4;/*0.0000200814;*/ //диван комп

    private double lat1 = 60.0578220897725; //диван
    private double lng1 = 30.420949459075928;

    private double lat2 = 60.05780200837197; //комп
    private double lng2 = 30.420895814895626;

    private double lat3 = 60.05782878356994; //стол кух
    private double lng3 = 30.420871675014496;

    private double targetLatitude;
    private double targetLongitude;

    private boolean closeWorkshift = false;

    private String id1 = "52414449-5553-4e45-5457-4f524b53434f";
    private String id2 = "52414449-5553-4e45-5457-4f524b53434d";
    private String id3 = "a134d0b2-1da2-1ba7-c94c-e8e00c9f7a2d";

    private double r1; //от дивана
    private double r2; //от кухни
    private double r3; //от компа

    private double latCurrent;
    private double lngCurrent;

    private User user;
    private Workshift workshift;
    private ArrayList<Task> taskList;
    private List<Room> roomList;

    private Task currentTask;

    private SharedPreferences sharedPreferences;
    private static final String APP_PREFERENCES = "preferences";
    private static final String APP_PREFERENCES_USERNAME = "username";
    private static final String APP_PREFERENCES_PASS = "password";
    private String userLogin;
    private String userPassword;


    private static final String GEOJSON_SOURCE_ID = "GEOJSONFILE";
    protected static final String TAG = "MapActivity";



    @Override
    public void onBackPressed() {
        // свой диалог на выход
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.main_tasks);
        //textView = (TextView) findViewById(R.id.textView);
        //menuButton = (Button) findViewById(R.id.mainMenu);
        completeTask = (Button) findViewById(R.id.completeTask);
        homeButton = (Button) findViewById(R.id.homeBtn);
        taskNameText = (TextView) findViewById(R.id.taskName);
        taskDescriptionText = (TextView) findViewById(R.id.taskDescription);
        taskLocationText = (TextView) findViewById(R.id.taskLocation);

        mapView = findViewById(R.id.taskMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Intent intent = getIntent();
        user = (User) intent.getParcelableExtra("user");
        workshift = (Workshift) intent.getParcelableExtra("workshift");
        taskList = intent.getParcelableArrayListExtra("tasks");

        try {
            boolean allTasksCompleted = true;
            for(Task task : taskList) {
                if(!task.getStatus().equals("completed")) {
                    targetLatitude = Double.parseDouble(task.getTaskLocation().getLatitude());
                    targetLongitude = Double.parseDouble(task.getTaskLocation().getLongitude());
                    allTasksCompleted = false;
                    currentTask = task;
                    updateTasksFields(currentTask);

                    break;
                }
            }

            if(allTasksCompleted) {
                targetLatitude = Double.parseDouble(taskList.get(0).getTaskLocation().getLatitude());
                targetLongitude = Double.parseDouble(taskList.get(0).getTaskLocation().getLongitude());
                completeTask.setText("Закрыть смену");
                taskNameText.setText("Все задачи завершены");
                taskDescriptionText.setText("Нет активных задач");
                taskLocationText.setText("");
                closeWorkshift = true;
            } else {
                Coordinate coordinate = new Coordinate(Double.parseDouble(currentTask.getLocation().getLatitude()),
                        Double.parseDouble(currentTask.getLocation().getLongitude()));
                sendLocation(coordinate);
                Log.i("Current Location", String.valueOf(coordinate.getLatitude()));
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        userLogin = sharedPreferences.getString(APP_PREFERENCES_USERNAME, "");
        userPassword = sharedPreferences.getString(APP_PREFERENCES_PASS, "");

        /*menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MapActivity.this, UserHomeActivity.class);
                intent1.putExtra("user", user);
                intent1.putExtra("workshift", workshift);
                intent1.putExtra("tasks", (Serializable) taskList);
                startActivity(intent1);
            }
        });

         */

        NetworkService.setCredentials(userLogin, userPassword);
        NetworkService.getInstance()
                .getIndoorNavigationApi()
                .getRooms()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                        if(response.message().equals("OK")) {
                            ApiResponse apiResponse = response.body();

                            Type listType = new TypeToken<ArrayList<Room>>(){}.getType();
                            roomList = new Gson().fromJson(apiResponse.getData(), listType);

                        } else {
                            showToast(getText(R.string.wrongCredentials).toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        showToast(getText(R.string.badServerConnection).toString());
                    }
                });

        completeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean allTasksCompleted = true;
                for(Task task : taskList) {
                    if(!task.getStatus().equals("completed")) {
                        allTasksCompleted = false;
                        break;
                    }
                }

                if(!allTasksCompleted) {

                    int index = taskList.lastIndexOf(currentTask);
                    currentTask.setStatus("completed");
                    currentTask.setWorkshiftId(workshift.getWorkshiftId());
                    updateTask(currentTask);

                    Coordinate coordinate = new Coordinate(Double.parseDouble(currentTask.getTaskLocation().getLatitude()),
                            Double.parseDouble(currentTask.getTaskLocation().getLongitude()));
                    sendLocation(coordinate);

                    taskList.set(index, currentTask);
                    index++;

                    if(index < taskList.size()){
                        currentTask = taskList.get(index);
                        updateTasksFields(currentTask);

                        coordinate = new Coordinate(Double.parseDouble(currentTask.getTaskLocation().getLatitude()),
                                Double.parseDouble(currentTask.getTaskLocation().getLongitude()));
                        sendLocation(coordinate);

                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(Double.parseDouble(currentTask.getTaskLocation().getLatitude()),
                                        Double.parseDouble(currentTask.getTaskLocation().getLongitude())))
                                .zoom(15)
                                .tilt(20)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
                    } else {
                        completeTask.setText("Закрыть смену");
                        taskNameText.setText("Все задачи завершены");
                        taskDescriptionText.setText("Нет активных задач");
                        taskLocationText.setText("");
                        closeWorkshift = true;
                    }

                } else {

                    /*
                    allTasksCompleted = true;
                    for(Task task : taskList) {
                        if(!task.getStatus().equals("completed")) {
                            allTasksCompleted = false;
                            break;
                        }
                    }

                     */

                    if(closeWorkshift) {
                        Intent intent = new Intent(MapActivity.this, CloseWorkshiftActivity.class);
                        intent.putExtra("workshift", workshift);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }

                    if(allTasksCompleted) {
                        completeTask.setText("Закрыть смену");
                        taskNameText.setText("Все задачи завершены");
                        taskDescriptionText.setText("Нет активных задач");
                        taskLocationText.setText("");
                        closeWorkshift = true;
                    }

                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, UserHomeActivity.class);
                intent.putExtra("workshift", workshift);
                intent.putExtra("user", user);
                intent.putParcelableArrayListExtra("tasks", taskList);
                startActivity(intent);
            }
        });

        /*
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        levelButtons = findViewById(R.id.floor_level_buttons);

                        final List<Point> boundingBox = new ArrayList<>();

                        boundingBox.add(Point.fromLngLat(60.057777, 30.420808));
                        boundingBox.add(Point.fromLngLat(60.057777, 30.420808));
                        boundingBox.add(Point.fromLngLat(60.057922, 30.420621));
                        boundingBox.add(Point.fromLngLat(60.057922, 30.420621));

                        boundingBoxList = new ArrayList<>();
                        boundingBoxList.add(boundingBox);

                        mapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
                            @Override
                            public void onCameraMove() {
                                if (mapboxMap.getCameraPosition().zoom > 16) {
                                    if (TurfJoins.inside(Point.fromLngLat(mapboxMap.getCameraPosition().target.getLongitude(),
                                            mapboxMap.getCameraPosition().target.getLatitude()), Polygon.fromLngLats(boundingBoxList))) {
                                        if (levelButtons.getVisibility() != View.VISIBLE) {
                                            showLevelButton();
                                        }
                                    } else {
                                        if (levelButtons.getVisibility() == View.VISIBLE) {
                                            hideLevelButton();
                                        }
                                    }
                                } else if (levelButtons.getVisibility() == View.VISIBLE) {
                                    hideLevelButton();
                                }
                            }
                        });
                        indoorBuildingSource = new GeoJsonSource(
                                "indoor-building", loadJsonFromAsset("scheme.json"));
                        //style.addSource(indoorBuildingSource);
                        createGeoJsonSource(style);
                        // Add the building layers since we know zoom levels in range
                        loadBuildingLayer(style);
                    }
                });
                //indoorBuildingSource.setGeoJson(loadJsonFromAsset("exported.geojson"));
                Button buttonSecondLevel = findViewById(R.id.second_level_button);
                buttonSecondLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        indoorBuildingSource.setGeoJson(loadJsonFromAsset("scheme.geojson"));
                    }
                });

                Button buttonGroundLevel = findViewById(R.id.ground_level_button);
                buttonGroundLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        indoorBuildingSource.setGeoJson(loadJsonFromAsset("scheme.geojson"));
                    }
                });
            }
        });

         */
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setForegroundScanPeriod(2100);
        beaconManager.setForegroundBetweenScanPeriod(5000);
        beaconManager.setBackgroundScanPeriod(2100);
        beaconManager.setBackgroundBetweenScanPeriod(5000);


        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        markerViewManager = new MarkerViewManager(mapView, mapboxMap);

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                new LoadGeoJson(MapActivity.this).execute();
            }
        });

        View customView1 = LayoutInflater.from(MapActivity.this).inflate(
                R.layout.marker_view, null);
        customView1.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        View customView2 = LayoutInflater.from(MapActivity.this).inflate(
                R.layout.marker_view, null);
        customView2.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        View customView3 = LayoutInflater.from(MapActivity.this).inflate(
                R.layout.marker_view, null);
        customView3.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(targetLatitude, targetLongitude))
                .zoom(15)
                .tilt(20)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);

        try {


            for(Task task : taskList) {

                double latitude = Double.parseDouble(task.getTaskLocation().getLatitude());
                double longitude = Double.parseDouble(task.getTaskLocation().getLongitude());
                View customView = LayoutInflater.from(MapActivity.this).inflate(
                        R.layout.marker_layout, null);
                //TextView roomName = customView.findViewById(R.id.nameRoom);
                //roomName.setText(task.getLocationName());
                //TextView markerText = customView.findViewById(R.id.markerText);
                //markerText.setText(task.getTaskLocation().getLocationName());

                MarkerView markerView = new MarkerView(new LatLng(latitude, longitude), customView);
                markerViewManager.addMarker(markerView);
            }



            for(Room room : roomList) {
                double latitude = Double.parseDouble(room.getLatitude());
                double longitude = Double.parseDouble(room.getLongitude());
                String name = room.getRoomName();

                View customView = LayoutInflater.from(MapActivity.this).inflate(
                        R.layout.room_description, null);
                TextView roomName = customView.findViewById(R.id.roomName);
                roomName.setText(name);
                MarkerView markerView = new MarkerView(new LatLng(latitude, longitude), customView);
                markerViewManager.addMarker(markerView);
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        /*
        View roomView1 = LayoutInflater.from(MapActivity.this).inflate(
                R.layout.room_description, null);
        TextView roomName1 = roomView1.findViewById(R.id.roomName);
        roomName1.setText("Цех 1");
        MarkerView roomMarker1 = new MarkerView(new LatLng(60.071596389670596, 30.409898757934574), roomView1);
        markerViewManager.addMarker(roomMarker1);

        View roomView2 = LayoutInflater.from(MapActivity.this).inflate(
                R.layout.room_description, null);
        TextView roomName2 = roomView1.findViewById(R.id.roomName);
        roomName2.setText("Цех 2");
        MarkerView roomMarker2 = new MarkerView(new LatLng(60.072859627064304, 30.415027141571045), roomView2);
        markerViewManager.addMarker(roomMarker2);

        View roomView3 = LayoutInflater.from(MapActivity.this).inflate(
                R.layout.room_description, null);
        TextView roomName3 = roomView1.findViewById(R.id.roomName);
        roomName3.setText("Цех 3");
        MarkerView roomMarker3 = new MarkerView(new LatLng(60.07035451592185, 30.412108898162842), roomView3);
        markerViewManager.addMarker(roomMarker3);

         */

        /*
        MarkerView markerView1 = new MarkerView(new LatLng(60.0578220897725, 30.420949459075928), customView1);
        MarkerView markerView2 = new MarkerView(new LatLng(60.05780200837197, 30.420895814895626), customView2);
        MarkerView markerView3 = new MarkerView(new LatLng(60.05782878356994, 30.420871675014496), customView3);

        markerViewManager.addMarker(markerView1);
        markerViewManager.addMarker(markerView2);
        markerViewManager.addMarker(markerView3);

         */

    }

    private void createGeoJsonSource(@NonNull Style loadedMapStyle) {
        try {
// Load data from GeoJSON file in the assets folder
            loadedMapStyle.addSource(new GeoJsonSource(GEOJSON_SOURCE_ID,
                    new URI("asset://scheme.geojson")));
        } catch (URISyntaxException exception) {
            //Timber.d(exception);
        }
    }

    private void drawLines(@NonNull FeatureCollection featureCollection) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(style -> {
                if (featureCollection.features() != null) {
                    if (featureCollection.features().size() > 0) {
                        style.addSource(new GeoJsonSource("line-source", featureCollection));

                        // The layer properties for our line. This is where we make the line dotted, set the
                        // color, etc.
                        style.addLayer(new LineLayer("linelayer", "line-source")
                                .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                        PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                        PropertyFactory.lineOpacity(.6f),
                                        PropertyFactory.lineWidth(6f),
                                        PropertyFactory.lineColor(Color.parseColor("#FF00A9B0"))));
                    }
                }
            });
        }
    }


    private static class LoadGeoJson extends AsyncTask<Void, Void, FeatureCollection> {

        private WeakReference<MapActivity> weakReference;

        LoadGeoJson(MapActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(Void... voids) {
            try {
                MapActivity activity = weakReference.get();
                if (activity != null) {
                    InputStream inputStream = activity.getAssets().open("scheme.geojson");

                    return FeatureCollection.fromJson(convertStreamToString(inputStream));
                }
            } catch (Exception exception) {

            }
            return null;
        }

        static String convertStreamToString(InputStream is) {
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }

        @Override
        protected void onPostExecute(@Nullable FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);
            MapActivity activity = weakReference.get();
            if (activity != null && featureCollection != null) {
                activity.drawLines(featureCollection);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void hideLevelButton() {
        // When the user moves away from our bounding box region or zooms out far enough the floor level
        // buttons are faded out and hidden.
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        levelButtons.startAnimation(animation);
        levelButtons.setVisibility(View.GONE);
    }

    private void showLevelButton() {
        // When the user moves inside our bounding box region or zooms in to a high enough zoom level,
        // the floor level buttons are faded out and hidden.
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        levelButtons.startAnimation(animation);
        levelButtons.setVisibility(View.VISIBLE);
    }

    private void loadBuildingLayer(@NonNull Style style) {
        // Method used to load the indoor layer on the map. First the fill layer is drawn and then the
        // line layer is added.

        FillLayer indoorBuildingLayer = new FillLayer("indoor-building-fill", "indoor-building").withProperties(
                fillColor(Color.parseColor("#eeeeee")),
                // Function.zoom is used here to fade out the indoor layer if zoom level is beyond 16. Only
                // necessary to show the indoor map at high zoom levels.
                fillOpacity(interpolate(exponential(1f), zoom(),
                        stop(16f, 0f),
                        stop(16.5f, 0.5f),
                        stop(17f, 1f))));

        style.addLayer(indoorBuildingLayer);

        LineLayer indoorBuildingLineLayer = new LineLayer("indoor-building-line", "indoor-building").withProperties(
                lineColor(Color.parseColor("#50667f")),
                lineWidth(0.5f),
                lineOpacity(interpolate(exponential(1f), zoom(),
                        stop(16f, 0f),
                        stop(16.5f, 0.5f),
                        stop(17f, 1f))));
        style.addLayer(indoorBuildingLineLayer);
    }

    private String loadJsonFromAsset(String filename) {
        // Using this method to load in GeoJSON files from the assets folder.

        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, Charset.forName("UTF-8"));

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }



    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    //textView.setText("The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    //beacons.iterator().next().getBeaconTypeCode();

                    String showStirng = new String();
                    distances = new double[beacons.size()];
                    int i = 0;
                    for (Beacon beacon : beacons) {
                        showStirng += "Distance: " + beacon.getDistance() + " Id: " + beacon.getId1() + "\r\n";

                        if(beacon.getId1().toString().equals(id1)) {
                            r1 = beacon.getDistance();
                        } else if(beacon.getId1().toString().equals(id2)) {
                            r3 = beacon.getDistance();
                        } else if(beacon.getId1().toString().equals(id3)) {
                            r2 = beacon.getDistance();
                            Log.i(TAG, String.valueOf(beacon.getTxPower()));
                        }

                        //distances[i] = beacon.getDistance();
                        //Log.i(TAG, beacon.getId1().toString());
                        i++;
                    }

                    TimedBeaconSimulator timedBeaconSimulator = new TimedBeaconSimulator();
                    timedBeaconSimulator.createBasicSimulatedBeacons();
                    for(Beacon beacon : timedBeaconSimulator.getBeacons()) {

                    }
                    calculateLocation();
                    showStirng += "Current pos: " + latCurrent + " " + lngCurrent;
                    textView.setText(showStirng);
                    /*
                    View customView3 = LayoutInflater.from(MapActivity.this).inflate(
                            R.layout.marker_view, null);
                    customView3.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                    MarkerView markerView = new MarkerView(new LatLng(latCurrent, lngCurrent), customView3);
                    markerViewManager.addMarker(markerView);
                    */
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    private void calculateLocation() {

        latCurrent = ((r1*r1 - r2*r2 + delta1_3*delta1_3) / 2*delta1_3);
        lngCurrent = ((r1*r1 - r3*r3 + delta1_2*delta1_2) / 2*delta1_2);
    }

    private void goToPickerActivity() {
        startActivityForResult(
                new PlacePicker.IntentBuilder()
                        .accessToken(getString(R.string.mapbox_access_token))
                        .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(targetLatitude, targetLongitude)).zoom(16).build())
                                .build())
                        .build(this), REQUEST_CODE);
    }

    private void showToast(String show) {
        Toast toast = Toast.makeText(getApplicationContext(),
                show,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    private void updateTasksFields(Task task) {
        taskNameText.setText(task.getName());
        taskDescriptionText.setText(task.getDescription());
        taskLocationText.setText("Местоположение: " + task.getTaskLocation().getLocationName());
    }

    private void updateTask(Task task) {
        NetworkService.setCredentials(userLogin, userPassword);
        NetworkService.getInstance()
                .getIndoorNavigationApi()
                .updateTask(task)
                .enqueue(new Callback<Task>() {
                    @Override
                    public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {

                    }

                    @Override
                    public void onFailure(Call<Task> call, Throwable t) {
                        showToast(getText(R.string.badServerConnection).toString());
                    }
                });
    }

    private void sendLocation(Coordinate coordinate) {
        LocationService.setWorkshiftUUID(UUID.fromString(workshift.getWorkshiftId()));
        LocationService.getInstance().getLocationData().addLocationPoint(coordinate);

        NetworkService.setCredentials(userLogin, userPassword);
        NetworkService.getInstance()
                .getIndoorNavigationApi()
                .addLocation(LocationService.getInstance().getLocationJsonData())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        showToast(getText(R.string.badServerConnection).toString());
                    }
                });
    }

    protected void sendMap(ApiResponse apiResponse) {

        NetworkService.setCredentials(userLogin, userPassword);
        NetworkService.getInstance()
                .getIndoorNavigationApi()
                .addMap(apiResponse)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        showToast(getText(R.string.badServerConnection).toString());
                    }
                });
    }

}

