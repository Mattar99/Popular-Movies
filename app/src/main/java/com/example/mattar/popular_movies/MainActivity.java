package com.example.mattar.popular_movies;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.mattar.popular_movies.Adapters.MoviesAdapter;
import com.example.mattar.popular_movies.Model.MainResponse;
import com.example.mattar.popular_movies.Model.MoviesResponse;
import com.example.mattar.popular_movies.NetworkUtils.APIClient;
import com.example.mattar.popular_movies.NetworkUtils.NetworkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.recyclerview)
     RecyclerView recyclerView;

    private MoviesAdapter moviesAdapter;

    private List Movies ;
    private MainResponse mainResponse;

    private GridLayoutManager gridLayoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        Map<String,String> map = NetworkUtils.buildQueries(NetworkUtils.sort_order.POPULARITY.toString(),1);

        Call<MainResponse> call = APIClient.getApiInterface().getMovies(map);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                mainResponse = response.body();
                Movies = mainResponse.getResults();

                moviesAdapter = new MoviesAdapter(Movies);
                recyclerView.setAdapter(moviesAdapter);
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Log.i("Fail",t.getMessage());
            }
        });



    }






}
