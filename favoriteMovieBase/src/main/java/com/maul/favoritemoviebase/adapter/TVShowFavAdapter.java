package com.maul.favoritemoviebase.adapter;

import android.content.Context;
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
import com.maul.favoritemoviebase.R;
import com.maul.favoritemoviebase.modal.TVShow;


public class TVShowFavAdapter extends RecyclerView.Adapter<TVShowFavAdapter.ViewHolder> {

    private Cursor cursorTVS;

    public TVShowFavAdapter(Context context) {
        Context tContext = context;
    }

    public void setTVSList(Cursor tvsList) {
        this.cursorTVS = tvsList;
    }

    @NonNull
    @Override
    public TVShowFavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fav, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowFavAdapter.ViewHolder holder, int i) {
        final TVShow tvs = getItem(i);
        holder.title.setText(tvs.getOriginalName());
        Glide.with(holder.itemView.getContext())
                .load(tvs.getPosterPathFav())
                .apply(new RequestOptions().override(152, 228))
                .into(holder.poster);
    }

    private TVShow getItem(int position) {
        if (!cursorTVS.moveToPosition(position)) {
            throw new IllegalStateException("Invalid");
        }
        return new TVShow(cursorTVS);
    }

    @Override
    public int getItemCount() {
        if (cursorTVS == null) return 0;
        return cursorTVS.getCount();    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView poster;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleFavBase);
            poster = itemView.findViewById(R.id.posterFavBase);
        }
    }
}
