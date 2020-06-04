package com.example.indoornavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
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

public class SplashActiviy extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String APP_PREFERENCES = "preferences";
    private static final String APP_PREFERENCES_USERNAME = "username";
    private static final String APP_PREFERENCES_PASS = "password";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(APP_PREFERENCES_USERNAME) &&
                sharedPreferences.contains(APP_PREFERENCES_PASS)) {

            try {
                String userLogin = sharedPreferences.getString(APP_PREFERENCES_USERNAME, "");
                String userPassword = sharedPreferences.getString(APP_PREFERENCES_PASS, "");
                NetworkService.setCredentials(userLogin, userPassword);
                NetworkService.getInstance()
                        .getIndoorNavigationApi()
                        .getUser(Integer.parseInt(userLogin))
                        .enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                                if(response.message().equals("OK")) {
                                    ApiResponse apiResponse = response.body();
                                    user = new Gson().fromJson(apiResponse.getData(), User.class);
                                    Intent intent = new Intent(SplashActiviy.this, UserHomeActivity.class);
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

        } else {
            Intent intent = new Intent(SplashActiviy.this, LoginActivity.class);
            startActivity(intent);
        }
    }


    private void showToast(String show) {
        Toast toast = Toast.makeText(getApplicationContext(),
                show,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
