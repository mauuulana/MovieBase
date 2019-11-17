package com.maul.moviebase.adapter;

import android.content.Context;
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
import com.maul.moviebase.modal.Movie;

import java.util.ArrayList;

public class MovieThumbnailAdapter extends RecyclerView.Adapter<MovieThumbnailAdapter.ViewHolder> {

    private ArrayList<Movie> listMovie = new ArrayList<>();

    public MovieThumbnailAdapter(Context context) {
    }

    public void setData(ArrayList<Movie> item) {
        listMovie.clear();
        listMovie.addAll(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieThumbnailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        Movie movies = listMovie.get(i);

        holder.title.setText(movies.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(movies.getPosterPath())
                .apply(new RequestOptions().override(202, 253))
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView poster;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            poster = itemView.findViewById(R.id.poster);
        }
    }
}