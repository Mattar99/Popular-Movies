package com.example.mattar.popular_movies;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mattar.popular_movies.Adapters.EndlessRecyclerViewScrollListener;
import com.example.mattar.popular_movies.Adapters.MoviesAdapter;
import com.example.mattar.popular_movies.Model.MainResponse;
import com.example.mattar.popular_movies.Model.MoviesResponse;
import com.example.mattar.popular_movies.NetworkUtils.APIClient;
import com.example.mattar.popular_movies.NetworkUtils.NetworkUtils;

import java.util.ArrayList;
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
    @BindView(R.id.ProgressBar)
     ProgressBar progressBar;

    private GridLayoutManager gridLayoutManager;
    private MoviesAdapter moviesAdapter;
    private List<MoviesResponse> Movies ;
    private MainResponse mainResponse;



    private Parcelable mListState ;
    private String LIST_STATE_KEY = "LIST_STATE_KEY";
    private String ADAPTER_STATE_KEY = "ADAPTER_STATE_KEY";
    private String PAGE_KEY = "PAGE_KEY";



    private APIClient.API_interface apiInterface;

    private int page_number=1;
    private final Map<String,String> queries = NetworkUtils.buildQueries(NetworkUtils.sort_order.POPULARITY.toString(),page_number);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        if(savedInstanceState==null) {
            progressBar.setVisibility(View.VISIBLE);

            Movies = new ArrayList<>();
            moviesAdapter = new MoviesAdapter(Movies, MainActivity.this);

            loadMovies(queries);
        }else{
            page_number = savedInstanceState.getInt(PAGE_KEY);
            Log.i("Pagenumber",page_number+"");
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            Movies = savedInstanceState.getParcelableArrayList(ADAPTER_STATE_KEY);
            moviesAdapter = new MoviesAdapter(Movies, MainActivity.this);
            gridLayoutManager.onRestoreInstanceState(mListState);
        }

        recyclerView.setAdapter(moviesAdapter);




        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page = page_number;
                queries.put(NetworkUtils.PAGE_PARAM,String.valueOf(page));
                progressBar.setVisibility(View.VISIBLE);
                loadMovies(queries);

            }
        });

    }



    public void loadMovies(Map<String,String> map){

        Call<MainResponse> call = APIClient.getApiInterface().getMovies(map);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                mainResponse = response.body();
  /*              assert mainResponse != null;
                Movies = mainResponse.getResults();

                moviesAdapter = new MoviesAdapter(Movies,MainActivity.this);
                recyclerView.setAdapter(moviesAdapter);*/

                if(mainResponse!=null&&page_number<mainResponse.getTotalPages()){
                    Movies = mainResponse.getResults();
                    moviesAdapter.addMovies(Movies);
                    page_number++;
                }else{
                    Toast.makeText(MainActivity.this,"No more Movies Available..",Toast.LENGTH_LONG).show();
                }
                if(progressBar.getVisibility()==View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Log.i("RetrofitOnCallFailure",t.getMessage());
            }
        });
    }



    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = gridLayoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
        state.putParcelableArrayList(ADAPTER_STATE_KEY,moviesAdapter.getmMovies());
        state.putInt(PAGE_KEY,page_number);

    }





}
