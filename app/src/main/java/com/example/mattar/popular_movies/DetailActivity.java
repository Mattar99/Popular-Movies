package com.example.mattar.popular_movies;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mattar.popular_movies.Model.MoviesResponse;
import com.example.mattar.popular_movies.NetworkUtils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.movie_cover)
    ImageView movie_cover;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.release_date_content)
    TextView release_date_content;

    @BindView(R.id.overview_content)
    TextView overview_content;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        MoviesResponse movie = intent.getParcelableExtra("Movie");

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(movie.getOriginalTitle());
        }






        Picasso.get().load(NetworkUtils.BASE_COVER_URL+movie.getBackdropPath()).into(movie_cover);
        Log.i("url",NetworkUtils.BASE_COVER_URL+movie.getBackdropPath());
        ratingBar.setRating((float) (movie.getVoteAverage())/2);

        release_date_content.setText(movie.getReleaseDate());

        overview_content.setText(movie.getOverview());

    }

    


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
