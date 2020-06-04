package com.example.indoornavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.indoornavigation.data.NetworkService;
import com.example.indoornavigation.data.Report;
import com.example.indoornavigation.data.Task;
import com.example.indoornavigation.data.User;
import com.example.indoornavigation.data.Workshift;
import com.example.indoornavigation.data.WorkshiftResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloseWorkshiftActivity extends AppCompatActivity {

    private Button closeWorkshift;
    private EditText workerName;
    private EditText workerSurname;
    private EditText tabelId;
    private EditText reportContent;

    private User user;
    private Workshift workshift;

    private SharedPreferences sharedPreferences;
    private static final String APP_PREFERENCES = "preferences";
    private static final String APP_PREFERENCES_USERNAME = "username";
    private static final String APP_PREFERENCES_PASS = "password";
    private String userLogin;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_layout);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        userLogin = sharedPreferences.getString(APP_PREFERENCES_USERNAME, "");
        userPassword = sharedPreferences.getString(APP_PREFERENCES_PASS, "");

        closeWorkshift = (Button) findViewById(R.id.closeWorkshiftBtn);
        workerName = (EditText) findViewById(R.id.workerName);
        //workerSurname = (EditText) findViewById(R.id.workerSurname);
        tabelId = (EditText) findViewById(R.id.tabelId);
        reportContent = (EditText) findViewById(R.id.reportContent);

        Intent intent = getIntent();
        user = (User) intent.getParcelableExtra("user");
        workshift = (Workshift) intent.getParcelableExtra("workshift");

        try {
            workerName.setText(user.getName());
            tabelId.setText(String.valueOf(user.getTabelId()));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        closeWorkshift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((!workerName.getText().toString().equals("")) &&
                        (!tabelId.getText().toString().equals("")) &&
                        (!reportContent.getText().toString().equals(""))) {
                    Report report = new Report();
                    report.setReport_id(UUID.randomUUID().toString());
                    report.setWorker_id(user.getWorkerId().toString());
                    report.setTime(getTimestamp().toString());
                    report.setContent(reportContent.getText().toString());

                    workshift.setEndTime(getTimestamp().toString());
                    workshift.setStatus("closed");
                    workshift.setReport_id(report.getReport_id());

                    WorkshiftResult workshiftResult = new WorkshiftResult();
                    String workshiftJson = new Gson().toJson(workshift);
                    String reportJson = new Gson().toJson(report);
                    workshiftResult.setWorkshift(workshiftJson);
                    workshiftResult.setReport(reportJson);

                    NetworkService.setCredentials(userLogin, userPassword);
                    NetworkService.getInstance()
                            .getIndoorNavigationApi()
                            .updateWorkshift(workshiftResult)
                            .enqueue(new Callback<WorkshiftResult>() {
                                @Override
                                public void onResponse(@NonNull Call<WorkshiftResult> call, @NonNull Response<WorkshiftResult> response) {
                                    Intent intent = new Intent(CloseWorkshiftActivity.this, UserHomeActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<WorkshiftResult> call, Throwable t) {
                                    //showToast(getText(R.string.badServerConnection).toString());
                                }
                            });
                }
            }
        });


    }

    private Timestamp getTimestamp() {
        Date date = new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        return timestamp;
    }
}
