package com.cs407.final_project;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import okhttp3.RequestBody;


public interface OpenAIApiService {
    @POST("v1/chat/completions")
    Call<ApiResponse> postText(@Body RequestBody body);
}
