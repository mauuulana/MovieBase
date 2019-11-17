package com.maul.moviebase.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.maul.moviebase.R;
import com.maul.moviebase.activity.DetailActivityMovie;
import com.maul.moviebase.activity.SearchMovieActivity;
import com.maul.moviebase.adapter.MovieCardAdapter;
import com.maul.moviebase.modal.Movie;
import com.maul.moviebase.modal.MovieData;
import com.maul.moviebase.utils.ItemClick;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Objects;

import static com.maul.moviebase.activity.SearchMovieActivity.SEARCH_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements View.OnClickListener {

    private final static String LIST_STATE_KEY = "STATE";
    private ArrayList<Movie> list = new ArrayList<>();
    private MovieData data;
    private RecyclerView rv_discover_movie;
    private int currentPage = 1;
    private MovieCardAdapter adapter;
    private boolean isFetchingMovies;
    private AVLoadingIndicatorView loading;
    private final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = MovieData.getInstance();

        TextView btnMore = view.findViewById(R.id.more_btn_movies);
        btnMore.setOnClickListener(this);

        loading = view.findViewById(R.id.loadingMovies);
        loading.setIndicator("LineScalePulseOutIndicator");

        rv_discover_movie = view.findViewById(R.id.rv_popular_movie);

        SearchView searchView = view.findViewById(R.id.search_movies);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent mIntent = new Intent(getActivity(), SearchMovieActivity.class);
                mIntent.putExtra(SearchMovieActivity.EXTRA_SEARCH, "Movie Results");
                mIntent.putExtra(SEARCH_MOVIE, query);
                (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                startActivity(mIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (savedInstanceState != null) {
            loading.setVisibility(View.INVISIBLE);
            final ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
            assert movies != null;
            list.addAll(movies);
            adapter = new MovieCardAdapter(getActivity());
            adapter.setData(movies);
            rv_discover_movie.setAdapter(adapter);
            rv_discover_movie.setLayoutManager(layoutManager);

            ItemClick.addTo(rv_discover_movie).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailActivityMovie.class);
                intent.putExtra(DetailActivityMovie.EXTRA_MOVIES_CATEGORIES, "Movies");
                intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, movies.get(position));
                (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                startActivity(intent);
            });

        } else {
            getMovies(1);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY, list);
    }


    private void getMovies(int page) {
        isFetchingMovies = true;
        data.getPopularMovie(page, new MovieData.OnGetPageMovie() {
            @Override
            public void onSuccess(int page, ArrayList<Movie> movies) {
                loading.setVisibility(View.INVISIBLE);
                Log.d("Movie Data", "Current Page = " + currentPage);
                if (adapter == null) {
                    adapter = new MovieCardAdapter(getContext());
                    adapter.setData(movies);
                    list.addAll(movies);
                    rv_discover_movie.setAdapter(adapter);
                    rv_discover_movie.setLayoutManager(layoutManager);

                    ItemClick.addTo(rv_discover_movie).setOnItemClickListener((recyclerView, position, v) -> {
                        Intent intent = new Intent(getActivity(), DetailActivityMovie.class);
                        intent.putExtra(DetailActivityMovie.EXTRA_MOVIES_CATEGORIES, "Movies");
                        intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, movies.get(position));
                        (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                        startActivity(intent);
                    });

                } else {
                    isFetchingMovies = false;
                    adapter.setData(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.more_btn_movies) {
            if (!isFetchingMovies) {
                loading.setVisibility(View.VISIBLE);
                getMovies(currentPage + 1);
            }
        }
    }
}
