package com.example.mattar.popular_movies.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mattar.popular_movies.MainActivity;
import com.example.mattar.popular_movies.Model.MoviesResponse;
import com.example.mattar.popular_movies.NetworkUtils.NetworkUtils;
import com.example.mattar.popular_movies.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mattar on 4/23/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<MoviesResponse> mMovies ;
    private Context context ;
    int imageHeight ;
    int imageWidth ;


    public MoviesAdapter(List<MoviesResponse> movies , Context context) {
        this.mMovies = movies;
        this.context = context;
        imageWidth = (int)context.getResources().getDimension(R.dimen.album_cover_width) ;
        imageHeight =  (int)context.getResources().getDimension(R.dimen.album_cover_height);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        MoviesResponse movie = mMovies.get(position);



        //Picasso.get().load(NetworkUtils.BASE_IMAGE_URL+movie.getPosterPath()).into(holder.movie_poster);
        String url = NetworkUtils.BASE_IMAGE_URL+movie.getPosterPath();
        final ImageView imageView = holder.movie_poster;
        final CardView cardView = holder.item_container;
        LoadImage_Color(url,imageView,cardView);

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
        @BindView(R.id.item_container)
        CardView item_container;

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

    public void LoadImage_Color(String url, final ImageView imageView, final CardView cardView) {

        Picasso.get().load(url)
                .resize(imageWidth,imageHeight)
                .centerCrop()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        assert imageView != null;
                        imageView.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getLightMutedSwatch();
                                        if (textSwatch == null) {
                                            return;
                                        }
                                        cardView.setBackgroundColor(textSwatch.getRgb());

                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

}
