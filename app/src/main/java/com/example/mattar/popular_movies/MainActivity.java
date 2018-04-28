package com.example.mattar.popular_movies;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;


    private Parcelable mListState ;
    private String LIST_STATE_KEY = "LIST_STATE_KEY";
    private String ADAPTER_STATE_KEY = "ADAPTER_STATE_KEY";
    private String PAGE_KEY = "PAGE_KEY";
    private String SORT_ORDER_STATE = "SORT_ORDER_STATE";



    private APIClient.API_interface apiInterface;

    //
    private int page_number=1;
    private final Map<String,String> queries = NetworkUtils.buildQueries(page_number);
    private String sort_order ="popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                page = page_number;
                queries.put(NetworkUtils.PAGE_PARAM,String.valueOf(page));
                progressBar.setVisibility(View.VISIBLE);
                loadMovies(queries);
            }
        };


        if(savedInstanceState==null) {
            progressBar.setVisibility(View.VISIBLE);

            Movies = new ArrayList<>();
            moviesAdapter = new MoviesAdapter(Movies, MainActivity.this);

            loadMovies(queries);
        }else{
            page_number = savedInstanceState.getInt(PAGE_KEY);
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            Movies = savedInstanceState.getParcelableArrayList(ADAPTER_STATE_KEY);
            moviesAdapter = new MoviesAdapter(Movies, MainActivity.this);
            gridLayoutManager.onRestoreInstanceState(mListState);
            sort_order = savedInstanceState.getString(SORT_ORDER_STATE);
        }

        recyclerView.setAdapter(moviesAdapter);


        moviesAdapter.setOnItemClickListener(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("Movie", moviesAdapter.getmMovies().get(position));

                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);



    }



    public void loadMovies(Map<String,String> map){

        Call<MainResponse> call = APIClient.getApiInterface().getMovies(sort_order,map);

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
        state.putString(SORT_ORDER_STATE,sort_order);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.highestRated:
                    Reset(NetworkUtils.sort_order.VOTE.toString());
                return true;
            case R.id.mostPopular:
                Reset(NetworkUtils.sort_order.POPULARITY.toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void Reset(String path){
        Movies = new ArrayList<>();
        moviesAdapter.setmMovies(new ArrayList<MoviesResponse>(Movies));
        page_number=1;
        sort_order = path;
        queries.put(NetworkUtils.PAGE_PARAM,String.valueOf(page_number));
        loadMovies(queries);
        endlessRecyclerViewScrollListener.resetState();
    }




}
