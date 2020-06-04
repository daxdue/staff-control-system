package com.example.indoornavigation.data;

import com.example.indoornavigation.data.interfaces.IndoorNavigationApi;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static NetworkService mInstance;
    private static final String BASE_URL = "http://192.168.1.124:4567/api/v1/";
    private static Retrofit mRetrofit;
    private static OkHttpClient okHttpClient;
    private static String mLogin;
    private static String mPassword;

    private NetworkService() {
         okHttpClient = new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    Request request = response.request();
                    if (request.header("Authorization") != null)
                        // Логин и пароль неверны
                        return null;
                    return request.newBuilder()
                            .header("Authorization", Credentials.basic(mLogin, mPassword))
                            .build();
                })
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if(mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public IndoorNavigationApi getIndoorNavigationApi() {
        return mRetrofit.create(IndoorNavigationApi.class);
    }

    public static void setCredentials(String login, String password) {
        mLogin = login;
        mPassword = password;
    }
}
