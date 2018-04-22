package com.example.mattar.popular_movies;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mattar.popular_movies.Model.MainResponse;
import com.example.mattar.popular_movies.Model.MoviesResponse;
import com.example.mattar.popular_movies.NetworkUtils.APIClient;
import com.example.mattar.popular_movies.NetworkUtils.NetworkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }






}
