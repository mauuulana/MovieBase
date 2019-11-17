package com.maul.moviebase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class SearchMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static String SEARCH_MOVIE = "query";
    public static String EXTRA_SEARCH = "extra_search";
    private RecyclerView rvSearchMovie;
    private MovieCardAdapter adapterMovie;
    private MovieData movieData;
    private AVLoadingIndicatorView loading;
    private TextView result;
    private ArrayList<Movie> listMovie = new ArrayList<>();


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        rvSearchMovie = findViewById(R.id.rv_result);
        loading = findViewById(R.id.loadingResult);
        loading.setIndicator("LineScalePulseOutIndicator");
        movieData = MovieData.getInstance();
        ImageView btnBackResult = findViewById(R.id.back_result);
        btnBackResult.setOnClickListener(this);
        result = findViewById(R.id.tv_result);

        String titleActivityResultMovie = getIntent().getStringExtra(EXTRA_SEARCH);
        TextView titleActivity = findViewById(R.id.title_activity_result);
        titleActivity.setText(titleActivityResultMovie);


        Intent intent = getIntent();
        String query = intent.getStringExtra(SEARCH_MOVIE);

        showRV();
        getSearchMovie(query);

    }

    private void showRV() {
        adapterMovie = new MovieCardAdapter(this);
        rvSearchMovie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSearchMovie.setHasFixedSize(true);
    }


    private void getSearchMovie(String query) {
        movieData.getSearchMovie(query, new MovieData.OnGetSearchMovie() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                loading.setVisibility(View.GONE);
                adapterMovie.setData(movies);
                rvSearchMovie.setAdapter(adapterMovie);
                result.setText(query);
                listMovie.addAll(movies);
                ItemClick.addTo(rvSearchMovie).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(SearchMovieActivity.this, DetailActivityMovie.class);
                    intent.putExtra(DetailActivityMovie.EXTRA_MOVIES_CATEGORIES, "Movies");
                    intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, listMovie.get(position));
                    SearchMovieActivity.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                    startActivity(intent);
                });
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_result) {
            onBackPressed();
            {
                finish();
            }
            this.overridePendingTransition(R.anim.no_anim, R.anim.slide_left);
        }

    }

}
