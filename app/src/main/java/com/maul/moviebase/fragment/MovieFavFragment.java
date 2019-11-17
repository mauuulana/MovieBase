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
import com.maul.moviebase.activity.DetailActivityMovie;
import com.maul.moviebase.adapter.MovieFavAdapter;
import com.maul.moviebase.db.MovieHelper;
import com.maul.moviebase.utils.ItemClick;

import java.util.Objects;

import static com.maul.moviebase.db.DatabaseContract.CONTENT_URI;


public class MovieFavFragment extends Fragment {


    private Cursor listMovie;
    private RecyclerView rvFavMovie;
    private MovieFavAdapter favMovieAdadpt;

    public MovieFavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_fav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavMovie = view.findViewById(R.id.rv_fav_movie);
        MovieHelper helper = MovieHelper.getInstance(getActivity());
        helper.open();

        new loadMovie().execute();
        showRVMovie();
    }

    private void showRVMovie() {
        favMovieAdadpt = new MovieFavAdapter(getActivity());
        rvFavMovie.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvFavMovie.setAdapter(favMovieAdadpt);
        rvFavMovie.setHasFixedSize(true);
        favMovieAdadpt.setMovieList(listMovie);

        ItemClick.addTo(rvFavMovie).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getActivity(), DetailActivityMovie.class);
            intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, "Movies");
            intent.putExtra(DetailActivityMovie.EXTRA_MOVIES_CATEGORIES, listMovie.getPosition());
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

    private class loadMovie extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            return Objects.requireNonNull(getContext()).getContentResolver()
                    .query(
                            CONTENT_URI,
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
        protected void onPostExecute(Cursor cursorMovies) {
            super.onPostExecute(cursorMovies);
            listMovie = cursorMovies;
            favMovieAdadpt.setMovieList(listMovie);
            favMovieAdadpt.notifyDataSetChanged();
        }
    }
}
