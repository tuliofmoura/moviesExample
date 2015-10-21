package com.example.movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movies.model.MovieEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


/**
 * Created by tuliomoura on 10/20/15.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieItemViewHolder> {

    private ArrayList<MovieEntity> mMovies;
    private Context mContext;

    public MoviesAdapter(Context context, ArrayList<MovieEntity> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MoviesAdapter.MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MovieItemViewHolder holder, int position) {
        final MovieEntity movie = getItem(position);
        holder.title.setText(movie.getTitle());
        holder.rating.setText(movie.getImdbRating());
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(movie.getPoster(), holder.poster);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(mContext, DetailsActivity.class);
                detailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movie.getId());
                mContext.startActivity(detailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public MovieEntity getItem(int position) {
        return mMovies.get(position);
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder {

        View root;
        ImageView poster;
        TextView title;
        TextView rating;

        public MovieItemViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            poster = (ImageView) itemView.findViewById(R.id.item_movie_poster);
            title = (TextView) itemView.findViewById(R.id.item_movie_title);
            rating = (TextView) itemView.findViewById(R.id.item_movie_rating);
        }
    }
}
