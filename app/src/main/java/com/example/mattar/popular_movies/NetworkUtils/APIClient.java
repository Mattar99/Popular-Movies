package com.example.mattar.popular_movies.NetworkUtils;

import com.example.mattar.popular_movies.Model.MainResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Mattar on 4/22/2018.
 */


public class APIClient  {

    private static final String BASE_URL ="https://api.themoviedb.org/3/";

    private static Retrofit retrofit  = null ;


    public static Retrofit getAPIClient()
    {
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return
                retrofit;
    }


    public static API_interface getApiInterface(){
        return getAPIClient().create(API_interface.class);
    }


    public interface API_interface {

        @GET("discover/movie")
        Call<MainResponse> getMovies(@QueryMap Map<String, String> Queries);

    }


}