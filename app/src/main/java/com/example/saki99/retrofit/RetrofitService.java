package com.example.saki99.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Saki99 on 8.2.2018..
 */

public interface RetrofitService {

    @GET("posts")
    Call<List<Podatak>> getAllData();

    @GET("posts/{id}")
    Call<Podatak> getDataById(@Path("id") int id);
}
