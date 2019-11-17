package com.maul.moviebase.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maul.moviebase.R;
import com.maul.moviebase.activity.DetailActivityTVShow;
import com.maul.moviebase.adapter.TVShowFavAdapter;
import com.maul.moviebase.db.TVShowHelper;
import com.maul.moviebase.utils.ItemClick;

import java.util.Objects;

import static com.maul.moviebase.db.DatabaseContract.CONTENT_URI_TV;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFavFragment extends Fragment {

    private Cursor listTVS;
    private RecyclerView rvFavTVS;
    private TVShowFavAdapter favTvsAdadpt;

    public TVShowFavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow_fav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavTVS = view.findViewById(R.id.rv_fav_tvs);
        TVShowHelper helper = TVShowHelper.getInstance(getActivity());
        helper.open();

        new loadTVS().execute();
        showRVTvs();
    }

    private void showRVTvs() {
        favTvsAdadpt = new TVShowFavAdapter(getActivity());
        rvFavTVS.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvFavTVS.setAdapter(favTvsAdadpt);
        rvFavTVS.setHasFixedSize(true);
        favTvsAdadpt.setTVSList(listTVS);
        ItemClick.addTo(rvFavTVS).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailActivityTVShow.class);
            intent.putExtra(DetailActivityTVShow.EXTRA_TVS_CATEGORIES, listTVS.getPosition());
            intent.putExtra(DetailActivityTVShow.EXTRA_TVS, "TV Show");
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class loadTVS extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            return Objects.requireNonNull(getContext()).getContentResolver()
                    .query(
                            CONTENT_URI_TV,
                            null,
                            null,
                            null,
                            null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursorTVS) {
            super.onPostExecute(cursorTVS);
            listTVS = cursorTVS;
            favTvsAdadpt.setTVSList(listTVS);
            favTvsAdadpt.notifyDataSetChanged();
        }
    }
}
