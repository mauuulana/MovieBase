package com.maul.moviebase.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.maul.moviebase.R;
import com.maul.moviebase.db.MovieHelper;
import com.maul.moviebase.modal.Genre;
import com.maul.moviebase.modal.Movie;
import com.maul.moviebase.modal.MovieData;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.maul.moviebase.db.DatabaseContract.CONTENT_URI;
import static com.maul.moviebase.db.DatabaseContract.FavoriteMovie.COLUMN_POSTER_PATH;
import static com.maul.moviebase.db.DatabaseContract.FavoriteMovie.COLUMN_TITLE;


public class DetailActivityMovie extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_MOVIES = "extra_movies";
    public static final String EXTRA_MOVIES_CATEGORIES = "movies";

    Integer movieId;
    private boolean isAdd = false;
    private Movie selectMovie;
    private ImageView backdrop, poster;
    private TextView tvTitle, tvOverview, tvGenre, tvPopularity, tvReleased, tvReleasedInfo, tvLanguage, tvRating, tvReviewer;
    private RatingBar barRating;
    private MovieData data;
    private ImageButton btnLove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        selectMovie = getIntent().getParcelableExtra(EXTRA_MOVIES);
        String categories = getIntent().getStringExtra(EXTRA_MOVIES_CATEGORIES);

        TextView tvCategories = findViewById(R.id.categories);
        tvCategories.setText(categories);

        ImageView backDetail = findViewById(R.id.back_detail);
        backDetail.setOnClickListener(this);

        btnLove = findViewById(R.id.btn_love);
        btnLove.setOnClickListener(v -> {
            if (isAdd) {
                removeFavorite();
            } else {
                addFavorite();
            }
            isAdd = !isAdd;
            if (isAdd) btnLove.setImageResource(R.drawable.ic_love);
            else btnLove.setImageResource(R.drawable.ic_unlove);
        });

        loadMovie();
        getMovie();
    }

    private void loadMovie() {
        MovieHelper helperMovie = MovieHelper.getInstance(getApplicationContext());
        helperMovie.open();
        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI + "/" + selectMovie.getId()),
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) isAdd = true;
            cursor.close();
        }

        if (isAdd) btnLove.setImageResource(R.drawable.ic_love);
        else btnLove.setImageResource(R.drawable.ic_unlove);
    }

    private void removeFavorite() {

        getContentResolver().delete(
                Uri.parse(CONTENT_URI + "/" + selectMovie.getId()),
                null,
                null
        );
        Toast.makeText(this, R.string.toast_unfav, Toast.LENGTH_LONG).show();
    }

    private void addFavorite() {
        ContentValues values = new ContentValues();

        values.put(_ID, selectMovie.getId());
        values.put(COLUMN_TITLE, selectMovie.getOriginalTitle());
        values.put(COLUMN_POSTER_PATH, selectMovie.getPosterPath());

        getContentResolver().insert(CONTENT_URI, values);

        Toast.makeText(this, R.string.toast_fav, Toast.LENGTH_LONG).show();

    }

    private void getMovie() {
        movieId = selectMovie.getId();
        data = MovieData.getInstance();
        data.getMovie(movieId, new MovieData.OnGetDetailMovie() {
            @Override
            public void onSuccess(Movie movie) {
                getGenres(movie);
                tvGenre = findViewById(R.id.genre_detail);

                poster = findViewById(R.id.poster_detail);
                Glide.with(DetailActivityMovie.this)
                        .load(selectMovie.getPosterPath())
                        .into(poster);
                backdrop = findViewById(R.id.img_backdrop);
                Glide.with(DetailActivityMovie.this)
                        .load(selectMovie.getBackdropPath())
                        .into(backdrop);

                tvTitle = findViewById(R.id.title_movies);
                tvTitle.setText(selectMovie.getOriginalTitle());

                tvReleased = findViewById(R.id.released_date);
                tvReleasedInfo = findViewById(R.id.released_detail_info);
                tvReleased.setText(selectMovie.getReleaseDate());
                tvReleasedInfo.setText(selectMovie.getReleaseDate());

                tvLanguage = findViewById(R.id.language_detail);
                tvLanguage.setText(selectMovie.getOriginalLanguage());

                tvOverview = findViewById(R.id.overview_detail);
                tvOverview.setText(selectMovie.getOverview());

                tvPopularity = findViewById(R.id.popularity_detail);
                tvPopularity.setText(String.valueOf(selectMovie.getPopularity()));

                tvRating = findViewById(R.id.rating_detail);
                barRating = findViewById(R.id.bar_rating);
                tvReviewer = findViewById(R.id.reviewer_detail);

                tvRating.setText(String.valueOf(selectMovie.getVoteAverage()));
                tvReviewer.setText(String.valueOf(selectMovie.getVoteCount()));

                double barMov = selectMovie.getVoteAverage() * 10;
                barRating.setRating((float) ((barMov * 5) / 100));
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getGenres(final Movie movie) {
        data.getGenre(new MovieData.OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    tvGenre.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void showError() {
        Toast.makeText(DetailActivityMovie.this, "Failed to load data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_detail) {
            onBackPressed();
            {
                finish();
            }
            this.overridePendingTransition(R.anim.no_anim, R.anim.slide_left);
        }
    }
}
