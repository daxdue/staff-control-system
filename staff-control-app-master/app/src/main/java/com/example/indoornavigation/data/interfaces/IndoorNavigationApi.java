package com.example.indoornavigation.data.interfaces;

import com.example.indoornavigation.data.ApiResponse;
import com.example.indoornavigation.data.Task;
import com.example.indoornavigation.data.Workshift;
import com.example.indoornavigation.data.WorkshiftResult;
import com.example.indoornavigation.location.LocationData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IndoorNavigationApi {

    @GET("get/users/{id}")
    public Call<ApiResponse> getUser(@Path("id") int id);

    @GET("get/tasks/{id}")
    public Call<ApiResponse> getUserTasks(@Path("id") int id);

    @GET("get/workshifts/{id}")
    public Call<ApiResponse> getWorkshift(@Path("id") int id);

    @GET("get/rooms")
    public Call<ApiResponse> getRooms();

    @POST("add/workshifts")
    public Call<Workshift> createWorkshift(@Body Workshift data);

    @PUT("add/tasks")
    public Call<Task> updateTask(@Body Task data);

    @PUT("add/workshifts")
    public Call<WorkshiftResult> updateWorkshift(@Body WorkshiftResult data);

    @POST("add/locations")
    public Call<ApiResponse> addLocation(@Body ApiResponse data);

    @POST("add/maps")
    public Call<ApiResponse> addMap(@Body ApiResponse data);
}
