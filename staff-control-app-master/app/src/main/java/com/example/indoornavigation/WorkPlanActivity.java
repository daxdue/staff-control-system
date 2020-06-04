package com.example.indoornavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.indoornavigation.data.ApiResponse;
import com.example.indoornavigation.data.NetworkService;
import com.example.indoornavigation.data.Task;
import com.example.indoornavigation.data.User;
import com.example.indoornavigation.data.Workshift;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkPlanActivity extends AppCompatActivity {

    private Button startWorkshift;
    private RecyclerView tasksList;
    private User user;
    private Workshift workshift;
    private SharedPreferences sharedPreferences;
    private static final String APP_PREFERENCES = "preferences";
    private static final String APP_PREFERENCES_USERNAME = "username";
    private static final String APP_PREFERENCES_PASS = "password";


    ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_plan);

        tasksList = (RecyclerView) findViewById(R.id.tasksList);
        startWorkshift = (Button) findViewById(R.id.startSmena);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        user = (User) intent.getParcelableExtra("user");

        taskList = new ArrayList<>();
        TaskAdapter taskAdapter = new TaskAdapter(this, taskList);
        tasksList.setAdapter(taskAdapter);

        if(sharedPreferences.contains(APP_PREFERENCES_USERNAME) &&
                sharedPreferences.contains(APP_PREFERENCES_PASS)) {

            try {
                String userLogin = sharedPreferences.getString(APP_PREFERENCES_USERNAME, "");
                String userPassword = sharedPreferences.getString(APP_PREFERENCES_PASS, "");

                NetworkService.setCredentials(userLogin, userPassword);
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
                                    taskAdapter.updateTasksList(taskList);

                                } else {
                                    showToast(getText(R.string.wrongCredentials).toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                showToast(getText(R.string.badServerConnection).toString());
                            }
                        });
            } catch (NullPointerException | NumberFormatException e) {
                e.printStackTrace();
                //showToast(getText(R.string.emptyField).toString());
            }


        }

        startWorkshift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(taskList.size() > 0) {
                    String userLogin = sharedPreferences.getString(APP_PREFERENCES_USERNAME, "");
                    String userPassword = sharedPreferences.getString(APP_PREFERENCES_PASS, "");

                    workshift = new Workshift();
                    workshift.setWorkshiftId(UUID.randomUUID().toString());
                    workshift.setWorkerId(user.getTabelId());
                    workshift.setStartTime(getTimestamp().toString());
                    workshift.setEndTime(getTimestamp().toString());
                    workshift.setReport_id(UUID.randomUUID().toString());
                    workshift.setStatus("active");

                    Log.i("Workshift", workshift.toString());

                    //user.setTasksList(taskList);

                    NetworkService.setCredentials(userLogin, userPassword);
                    NetworkService.getInstance()
                            .getIndoorNavigationApi()
                            .createWorkshift(workshift)
                            .enqueue(new Callback<Workshift>() {
                                @Override
                                public void onResponse(@NonNull Call<Workshift> call, @NonNull Response<Workshift> response) {
                                    Intent intent = new Intent(WorkPlanActivity.this, MapActivity.class);
                                    intent.putExtra("workshift", workshift);
                                    intent.putExtra("user", user);
                                    intent.putParcelableArrayListExtra("tasks", taskList);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<Workshift> call, Throwable t) {
                                    showToast(getText(R.string.badServerConnection).toString());
                                }
                            });
                } else {
                    showToast("Нет активных задач");
                }



            }
        });
    }



    private void showToast(String show) {
        Toast toast = Toast.makeText(getApplicationContext(),
                show,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    private Timestamp getTimestamp() {
        Date date = new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        return timestamp;
    }
}
