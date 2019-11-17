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
import com.maul.moviebase.adapter.TVShowCardAdapter;
import com.maul.moviebase.modal.Movie;
import com.maul.moviebase.modal.MovieData;
import com.maul.moviebase.modal.TVShow;
import com.maul.moviebase.modal.TVShowData;
import com.maul.moviebase.utils.ItemClick;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Objects;

public class SearchActivityMore extends AppCompatActivity implements View.OnClickListener {

    public static String SEARCH_MORE_TV = "query_tv";
    public static String SEARCH_MORE_MOVIE = "query_movie";
    public static String EXTRA_SEARCH = "extra_movie";
    public static String EXTRA_SEARCH_TVSHOW = "extra_tv";
    private RecyclerView rvSearchMore;
    private MovieCardAdapter adapterMovie;
    private TVShowCardAdapter adapterTVS;
    private MovieData movieData;
    private TVShowData tvsData;
    private AVLoadingIndicatorView loading;
    private TextView result;
    private ArrayList<Movie> listMovie = new ArrayList<>();
    private ArrayList<TVShow> listTVs = new ArrayList<>();


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_more);

        rvSearchMore = findViewById(R.id.rv_result_more);
        loading = findViewById(R.id.loadingMore);
        loading.setIndicator("LineScalePulseOutIndicator");
        movieData = MovieData.getInstance();
        tvsData = TVShowData.getInstance();
        ImageView btnBackResultMore = findViewById(R.id.back_more_result);
        btnBackResultMore.setOnClickListener(this);
        result = findViewById(R.id.tv_result_more);

        String titleActivityResult = getIntent().getStringExtra(EXTRA_SEARCH);
        String titleActivityResultMore = getIntent().getStringExtra(EXTRA_SEARCH_TVSHOW);
        TextView titleActivity1 = findViewById(R.id.title_result1);
        TextView titleActivity2 = findViewById(R.id.title_result2);

        titleActivity1.setText(titleActivityResultMore);
        titleActivity2.setText(titleActivityResult);

        Intent intent = getIntent();
        String queryMovie = intent.getStringExtra(SEARCH_MORE_MOVIE);
        String queryTV = intent.getStringExtra(SEARCH_MORE_TV);

        showRV();
        getSearchMovie(queryMovie);
        getSearchTV(queryTV);

    }

    private void showRV() {
        adapterMovie = new MovieCardAdapter(this);
        adapterTVS = new TVShowCardAdapter(this);
        rvSearchMore.setLayoutManager(new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvSearchMore.setHasFixedSize(true);
    }


    private void getSearchMovie(String query) {
        movieData.getSearchMovie(query, new MovieData.OnGetSearchMovie() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                loading.setVisibility(View.GONE);
                adapterMovie.setData(movies);
                rvSearchMore.setAdapter(adapterMovie);
                result.setText(query);
                listMovie.addAll(movies);
                ItemClick.addTo(rvSearchMore).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(SearchActivityMore.this, DetailActivityMovie.class);
                    intent.putExtra(DetailActivityMovie.EXTRA_MOVIES_CATEGORIES, "Movies");
                    intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, listMovie.get(position));
                    SearchActivityMore.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                    startActivity(intent);
                });
            }

            @Override
            public void onError() {

            }
        });
    }

    private void getSearchTV(String query) {
        tvsData.getSearchTv(query, new TVShowData.OnGetSearchTVShow() {
            @Override
            public void onSuccess(ArrayList<TVShow> tvs) {
                loading.setVisibility(View.GONE);
                adapterTVS.setData(tvs);
                rvSearchMore.setAdapter(adapterTVS);
                result.setText(query);
                listTVs.addAll(tvs);
                ItemClick.addTo(rvSearchMore).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(SearchActivityMore.this, DetailActivityTVShow.class);
                    intent.putExtra(DetailActivityTVShow.EXTRA_TVS_CATEGORIES, "TV Shows");
                    intent.putExtra(DetailActivityTVShow.EXTRA_TVS, listTVs.get(position));
                    SearchActivityMore.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
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
        if (v.getId() == R.id.back_more_result) {
            onBackPressed();
            {
                finish();
            }
            this.overridePendingTransition(R.anim.no_anim, R.anim.slide_left);
        }
    }
}
