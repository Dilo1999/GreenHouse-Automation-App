package com.example.gh_app;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("your/api/endpoint")
    Call<ApiResponse> getData();
}
