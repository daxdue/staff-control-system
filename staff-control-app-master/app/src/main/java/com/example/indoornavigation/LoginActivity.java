package com.example.indoornavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.indoornavigation.data.ApiResponse;
import com.example.indoornavigation.data.NetworkService;
import com.example.indoornavigation.data.User;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText login;
    private EditText password;
    private User user;
    private SharedPreferences sharedPreferences;
    private static final String APP_PREFERENCES = "preferences";
    private static final String APP_PREFERENCES_USERNAME = "username";
    private static final String APP_PREFERENCES_PASS = "password";
    private boolean authorized;

    private String userLogin;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    userLogin = login.getText().toString();
                    userPassword = password.getText().toString();
                    NetworkService.setCredentials(userLogin, userPassword);
                    NetworkService.getInstance()
                            .getIndoorNavigationApi()
                            .getUser(Integer.parseInt(login.getText().toString()))
                            .enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                                    if(response.message().equals("OK")) {
                                        ApiResponse apiResponse = response.body();
                                        user = new Gson().fromJson(apiResponse.getData(), User.class);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(APP_PREFERENCES_USERNAME, userLogin);
                                        editor.putString(APP_PREFERENCES_PASS, userPassword);
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);

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
                    showToast(getText(R.string.emptyField).toString());
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
    }


    private void showToast(String show) {
        Toast toast = Toast.makeText(getApplicationContext(),
                show,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

}
