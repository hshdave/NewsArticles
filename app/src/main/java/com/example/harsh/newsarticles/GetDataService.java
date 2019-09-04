package com.example.harsh.newsarticles;

import com.example.harsh.model.Articles;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("everything")
    Call<Articles> getAllArticles(@Query("q") String query,
                                  @Query("from") String from,
                                  @Query("sortBy") String sort,
                                  @Query("apiKey") String apik);
}
