package com.example.indoornavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoornavigation.data.ApiResponse;
import com.example.indoornavigation.data.LocationService;
import com.example.indoornavigation.data.NetworkService;
import com.example.indoornavigation.data.Task;
import com.example.indoornavigation.data.User;
import com.example.indoornavigation.data.Workshift;
import com.example.indoornavigation.location.Coordinate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeActivity extends AppCompatActivity  {

    private TextView workerName;
    private TextView workerPosition;
    private TextView workshiftInfo;

    private Button startWorkBtn;
    private Button stopWorkBtn;
    private Button pauseWorkBtn;
    private Button logOutBtn;
    private Button toTasksBtn;

    private User user;
    private Workshift workshift;
    private ArrayList<Task> taskList;

    private boolean workshiftGot = false;
    private boolean tasksGot = false;

    private SharedPreferences sharedPreferences;
    private static final String APP_PREFERENCES = "preferences";
    private static final String APP_PREFERENCES_USERNAME = "username";
    private static final String APP_PREFERENCES_PASS = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String userLogin = sharedPreferences.getString(APP_PREFERENCES_USERNAME, "");
        String userPassword = sharedPreferences.getString(APP_PREFERENCES_PASS, "");

        workerName = (TextView) findViewById(R.id.workerName);
        workerPosition = (TextView) findViewById(R.id.workerPosition);
        workshiftInfo = (TextView) findViewById(R.id.workshiftInfo);
        toTasksBtn = (Button) findViewById(R.id.toTaskBtn);
        startWorkBtn = (Button) findViewById(R.id.startWorkBtn);
        //stopWorkBtn = (Button) findViewById(R.id.stopWorkBtn);
        pauseWorkBtn = (Button) findViewById(R.id.pauseWorkBtn);
        logOutBtn = (Button) findViewById(R.id.logOutBtn);

        toTasksBtn.setVisibility(View.INVISIBLE);
        pauseWorkBtn.setVisibility(View.INVISIBLE);



        pauseWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(APP_PREFERENCES_USERNAME);
                editor.remove(APP_PREFERENCES_PASS);
                editor.apply();

                Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        user = (User) intent.getParcelableExtra("user");

        if(intent.hasExtra("workshift") && intent.hasExtra("tasks")) {
            taskList = intent.getParcelableArrayListExtra("tasks");
            workshift = (Workshift) intent.getParcelableExtra("workshift");
            try {
                if(workshift.getStatus().equals("active") && (taskList.size() > 0)) {
                    //Timestamp timestamp = Timestamp.valueOf(workshift.getStartTime());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                    //2020-05-24 20:10:07.331
                    Date date = dateFormat.parse(workshift.getStartTime());
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm dd.MM.yy");
                    String startTime = dateFormat1.format(date);

                    workshiftInfo.setText("Начата: " + startTime);
                    startWorkBtn.setText("Закрыть смену");
                    toTasksBtn.setVisibility(View.VISIBLE);
                    //pauseWorkBtn.setVisibility(View.VISIBLE);
                    workshiftGot = true;
                    tasksGot = true;
                }
            } catch (NullPointerException | ParseException e) {
                e.printStackTrace();
            }

        } else {
            NetworkService.setCredentials(userLogin, userPassword);
            NetworkService.getInstance()
                    .getIndoorNavigationApi()
                    .getWorkshift(user.getTabelId())
                    .enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                            ApiResponse apiResponse = response.body();

                            try {
                                if(apiResponse.getData() != null) {
                                    workshift = new Gson().fromJson(apiResponse.getData(), Workshift.class);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                                    //2020-05-24 20:10:07.331
                                    Date date = dateFormat.parse(workshift.getStartTime());
                                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm dd.MM.yy");
                                    String startTime = dateFormat1.format(date);
                                    toTasksBtn.setVisibility(View.VISIBLE);
                                    workshiftInfo.setText("Начата: " + startTime);
                                    startWorkBtn.setText("Закрыть смену");

                                    workshiftGot = true;
                                }

                            } catch (NullPointerException | ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {

                        }
                    });

            NetworkService.getInstance()
                    .getIndoorNavigationApi()
                    .getUserTasks(Integer.parseInt(userLogin))
                    .enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                            if(response.message().equals("OK")) {
                                ApiResponse apiResponse = response.body();

                                Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
                                taskList = new Gson().fromJson(apiResponse.getData(), listType);
                                tasksGot = true;
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                        }
                    });

            toTasksBtn.setVisibility(View.INVISIBLE);
            //pauseWorkBtn.setVisibility(View.INVISIBLE);
        }

        if(user != null) {
            workerName.setText(user.getName());
            workerPosition.setText(user.getPosition());
        }

        startWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(workshiftGot && tasksGot) {

                    boolean allTasksCompleted = true;
                    for(Task task : taskList) {
                        if(task.getStatus().equals("active")) {
                            allTasksCompleted = false;
                            break;
                        }
                    }

                    if(allTasksCompleted) {
                        Intent intent = new Intent(UserHomeActivity.this, CloseWorkshiftActivity.class);
                        intent.putExtra("workshift", workshift);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    } else {
                        showToast("Не все задачи завершены");
                    }

                } else {
                    Intent intent = new Intent(UserHomeActivity.this, WorkPlanActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });

        toTasksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(workshiftGot && tasksGot) {

                    Intent intent = new Intent(UserHomeActivity.this, MapActivity.class);
                    intent.putExtra("workshift", workshift);
                    intent.putExtra("user", user);
                    intent.putExtra("tasks", taskList);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // свой диалог на выход
    }

    private void showToast(String show) {
        Toast toast = Toast.makeText(getApplicationContext(),
                show,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.show();
    }

    private void sendLocation(Coordinate coordinate) {
        LocationService.setWorkshiftUUID(UUID.fromString(workshift.getWorkshiftId()));
        LocationService.getInstance().getLocationData().addLocationPoint(coordinate);

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
}
