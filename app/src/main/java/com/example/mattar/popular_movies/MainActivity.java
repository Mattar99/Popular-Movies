package com.example.mattar.popular_movies;

import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mattar.popular_movies.Adapters.EndlessRecyclerViewScrollListener;
import com.example.mattar.popular_movies.Adapters.MoviesAdapter;
import com.example.mattar.popular_movies.Model.MainResponse;
import com.example.mattar.popular_movies.Model.MoviesResponse;
import com.example.mattar.popular_movies.NetworkUtils.APIClient;
import com.example.mattar.popular_movies.NetworkUtils.NetworkUtils;

import java.util.List;
import java.util.Map;

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
    private int page_number;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        page_number = 1 ;
        final Map<String,String> map = NetworkUtils.buildQueries(NetworkUtils.sort_order.POPULARITY.toString(),page_number);

        Call<MainResponse> call = APIClient.getApiInterface().getMovies(map);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                mainResponse = response.body();
                Movies = mainResponse.getResults();

                moviesAdapter = new MoviesAdapter(Movies,MainActivity.this);
                recyclerView.setAdapter(moviesAdapter);
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Log.i("Fail",t.getMessage());
            }
        });



        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                map.put(NetworkUtils.PAGE_PARAM,String.valueOf(page));
                Call<MainResponse> call = APIClient.getApiInterface().getMovies(map);

                call.enqueue(new Callback<MainResponse>() {
                    @Override
                    public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {


                         mainResponse = response.body();



                        if(mainResponse!=null&&page_number<mainResponse.getTotalPages()){

                            Movies = mainResponse.getResults();
                            moviesAdapter.addMovies(Movies);

                        }else{

                            Toast.makeText(MainActivity.this,"No more Movies Available..",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<MainResponse> call, Throwable t) {
                        Log.i("Fail",t.getMessage());
                    }
                });
            }
        });

    }






}
