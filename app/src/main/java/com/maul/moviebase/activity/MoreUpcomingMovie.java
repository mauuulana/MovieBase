package com.maul.moviebase.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maul.moviebase.R;
import com.maul.moviebase.adapter.MovieCardAdapter;
import com.maul.moviebase.modal.Movie;
import com.maul.moviebase.modal.MovieData;
import com.maul.moviebase.utils.ItemClick;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static com.maul.moviebase.R.id.back_more;
import static com.maul.moviebase.activity.SearchActivityMore.SEARCH_MORE_MOVIE;

public class MoreUpcomingMovie extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TITLE = "extra_title";
    private final static String LIST_STATE_KEY = "STATE";
    private ArrayList<Movie> listMovieUpcoming = new ArrayList<>();
    private MovieData data;
    private RecyclerView rvMoreMovies;
    private int currentPage = 2;
    private MovieCardAdapter adapter;
    private boolean isFetchingMoviesMore;
    private AVLoadingIndicatorView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_home);

        String title = getIntent().getStringExtra(EXTRA_TITLE);

        TextView titleActivity = findViewById(R.id.title_activity_more);
        titleActivity.setText(title);

        ImageView backMore = findViewById(R.id.back_more);
        backMore.setOnClickListener(this);

        TextView btnMore = findViewById(R.id.more_btn_activity);
        btnMore.setOnClickListener(this);

        loading = findViewById(R.id.loadingMore);
        loading.setIndicator("LineScalePulseOutIndicator");

        rvMoreMovies = findViewById(R.id.rv_more);
        rvMoreMovies.setLayoutManager(new LinearLayoutManager(getParent(), LinearLayoutManager.VERTICAL, false));
        rvMoreMovies.setHasFixedSize(true);
        data = MovieData.getInstance();

        SearchView searchView = findViewById(R.id.search_more);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MoreUpcomingMovie.this, SearchActivityMore.class);
                intent.putExtra(SearchActivityMore.EXTRA_SEARCH, "Movie Results");
                intent.putExtra(SEARCH_MORE_MOVIE, query);
                MoreUpcomingMovie.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (savedInstanceState != null) {
            loading.setVisibility(INVISIBLE);
            final ArrayList<Movie> listMovie = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
            assert listMovie != null;
            listMovieUpcoming.addAll(listMovie);
            adapter = new MovieCardAdapter(getParent());
            adapter.setData(listMovie);
            rvMoreMovies.setAdapter(adapter);
            ItemClick.addTo(rvMoreMovies).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(MoreUpcomingMovie.this, DetailActivityMovie.class);
                intent.putExtra(DetailActivityMovie.EXTRA_MOVIES_CATEGORIES, "Movies");
                intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, listMovie.get(position));
                MoreUpcomingMovie.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                startActivity(intent);
            });

        } else {
            getData(2);

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY, listMovieUpcoming);
    }

    private void getData(int page) {
        isFetchingMoviesMore = true;
        data.getMovieUpcomingMore(page, new MovieData.OnGetPageMovie() {
            @Override
            public void onSuccess(int page, ArrayList<Movie> movies) {
                loading.setVisibility(View.INVISIBLE);
                Log.d("Movie Data", "Current Page = " + currentPage);

                if (adapter == null) {
                    adapter = new MovieCardAdapter(getBaseContext());
                    adapter.setData(movies);
                    rvMoreMovies.setAdapter(adapter);
                    loading.setVisibility(GONE);
                    listMovieUpcoming.addAll(movies);

                    ItemClick.addTo(rvMoreMovies).setOnItemClickListener((recyclerView, position, v) -> {
                        Intent intent = new Intent(MoreUpcomingMovie.this, DetailActivityMovie.class);
                        intent.putExtra(DetailActivityMovie.EXTRA_MOVIES_CATEGORIES, "Movies");
                        intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, movies.get(position));
                        MoreUpcomingMovie.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                        startActivity(intent);
                    });
                } else {
                    isFetchingMoviesMore = false;
                    adapter.setData(movies);
                }
                currentPage = page;
                isFetchingMoviesMore = false;
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.failure);
                Toast.makeText(getParent(), toast_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == back_more) {
            onBackPressed();
            {
                finish();
            }
            this.overridePendingTransition(R.anim.no_anim, R.anim.slide_left);
        } else if (view.getId() == R.id.more_btn_activity) {
            if (!isFetchingMoviesMore) {
                loading.setVisibility(View.VISIBLE);
                getData(currentPage + 1);
            }
        }
    }
}
