package com.example.mattar.popular_movies.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mattar.popular_movies.Model.MoviesResponse;
import com.example.mattar.popular_movies.NetworkUtils.NetworkUtils;
import com.example.mattar.popular_movies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mattar on 4/23/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<MoviesResponse> mMovies ;


    public MoviesAdapter(List<MoviesResponse> movies) {
        this.mMovies = movies;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MoviesResponse movie = mMovies.get(position);

        Picasso.get().load(NetworkUtils.BASE_IMAGE_URL+movie.getPosterPath()).into(holder.movie_poster);
        holder.movie_title.setText(movie.getOriginalTitle());
        holder.movie_overview.setText(movie.getOverview());

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_poster)
        ImageView movie_poster;
        @BindView(R.id.movie_title)
        TextView movie_title;
        @BindView(R.id.movie_overview)
        TextView movie_overview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void addMovies(List<MoviesResponse> movies){

        for (MoviesResponse mv : movies){
            this.mMovies.add(mv);
        }

        notifyDataSetChanged();
    }

}
