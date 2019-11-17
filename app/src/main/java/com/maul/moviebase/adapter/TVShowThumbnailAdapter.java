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
import com.maul.moviebase.modal.TVShow;

import java.util.ArrayList;

public class TVShowThumbnailAdapter extends RecyclerView.Adapter<TVShowThumbnailAdapter.ViewHolder> {

    private ArrayList<TVShow> data = new ArrayList<>();

    public TVShowThumbnailAdapter(Context context) {
    }

    public void setData(ArrayList<TVShow> item) {
        data.clear();
        data.addAll(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home, viewGroup, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        TVShow tvs = data.get(i);

        holder.title.setText(tvs.getOriginalName());
        Glide.with(holder.itemView.getContext())
                .load(tvs.getPosterPath())
                .apply(new RequestOptions().override(202, 253))
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return data.size();
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
