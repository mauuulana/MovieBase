package com.maul.moviebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.maul.moviebase.R;
import com.maul.moviebase.activity.DetailActivityMovie;
import com.maul.moviebase.modal.Movie;

public class MovieFavAdapter extends RecyclerView.Adapter<MovieFavAdapter.ViewHolder> {

    private Cursor cursorMovie;

    public MovieFavAdapter(Context context) {
        Context mContext = context;
    }

    public void setMovieList(Cursor movieList) {
        this.cursorMovie = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fav, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movies = getItem(position);
        holder.title.setText(movies.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(movies.getPosterPathFav())
                .apply(new RequestOptions().override(152, 228))
                .into(holder.poster);

        holder.poster.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivityMovie.class);
            intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, movies);
            holder.itemView.getContext().startActivity(intent);
        });

    }

    private Movie getItem(int position) {
        if (!cursorMovie.moveToPosition(position)) {
            throw new IllegalStateException("Invalid");
        }
        return new Movie(cursorMovie);
    }

    @Override
    public int getItemCount() {
        if (cursorMovie == null) return 0;
        return cursorMovie.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView poster;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleFav);
            poster = itemView.findViewById(R.id.posterFav);
        }
    }
}
