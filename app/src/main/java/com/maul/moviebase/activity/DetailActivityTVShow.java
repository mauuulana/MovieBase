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
import com.maul.moviebase.db.TVShowHelper;
import com.maul.moviebase.modal.Genre;
import com.maul.moviebase.modal.TVShow;
import com.maul.moviebase.modal.TVShowData;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.maul.moviebase.db.DatabaseContract.CONTENT_URI_TV;
import static com.maul.moviebase.db.DatabaseContract.FavoriteMovie.COLUMN_POSTER_PATH;
import static com.maul.moviebase.db.DatabaseContract.FavoriteMovie.COLUMN_TITLE;

public class DetailActivityTVShow extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TVS = "extra_tvs";
    public static final String EXTRA_TVS_CATEGORIES = "tvs";

    public int tvId;
    private boolean isAdd = false;
    private TVShow selectTV;
    private ImageView backdrop, poster;
    private TextView tvTitle, tvOverview, tvGenre, tvPopularity, tvReleased, tvReleasedInfo, tvLanguage, tvRating, tvReviewer;
    private RatingBar barRating;
    private TVShowData data;
    private ImageButton btnLove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        selectTV = getIntent().getParcelableExtra(EXTRA_TVS);
        String categories = getIntent().getStringExtra(EXTRA_TVS_CATEGORIES);

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
        loadTV();
        getTV();
    }

    private void loadTV() {
        TVShowHelper helperTVS = TVShowHelper.getInstance(getApplicationContext());
        helperTVS.open();
        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI_TV + "/" + selectTV.getId()),
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

    private void addFavorite() {
        ContentValues values = new ContentValues();

        values.put(_ID, selectTV.getId());
        values.put(COLUMN_TITLE, selectTV.getOriginalName());
        values.put(COLUMN_POSTER_PATH, selectTV.getPosterPath());

        getContentResolver().insert(CONTENT_URI_TV, values);

        Toast.makeText(this, R.string.toast_fav, Toast.LENGTH_LONG).show();
    }

    private void removeFavorite() {
        getContentResolver().delete(
                Uri.parse(CONTENT_URI_TV + "/" + selectTV.getId()),
                null,
                null
        );
        Toast.makeText(this, R.string.toast_unfav, Toast.LENGTH_LONG).show();
    }

    private void getTV() {
        tvId = selectTV.getId();
        data = TVShowData.getInstance();
        data.getTV(tvId, new TVShowData.OnGetDetailTVShow() {
            @Override
            public void onSuccess(TVShow tvs) {
                getGenres(tvs);
                tvGenre = findViewById(R.id.genre_detail);

                poster = findViewById(R.id.poster_detail);
                Glide.with(DetailActivityTVShow.this)
                        .load(selectTV.getPosterPath())
                        .into(poster);
                backdrop = findViewById(R.id.img_backdrop);
                Glide.with(DetailActivityTVShow.this)
                        .load(selectTV.getBackdropPath())
                        .into(backdrop);

                tvTitle = findViewById(R.id.title_movies);
                tvTitle.setText(selectTV.getOriginalName());

                tvReleased = findViewById(R.id.released_date);
                tvReleasedInfo = findViewById(R.id.released_detail_info);
                tvReleased.setText(selectTV.getFirstAirDate());
                tvReleasedInfo.setText(selectTV.getFirstAirDate());

                tvLanguage = findViewById(R.id.language_detail);
                tvLanguage.setText(selectTV.getOriginalLanguage());

                tvOverview = findViewById(R.id.overview_detail);
                tvOverview.setText(selectTV.getOverview());

                tvPopularity = findViewById(R.id.popularity_detail);
                tvPopularity.setText(String.valueOf(selectTV.getPopularity()));

                tvRating = findViewById(R.id.rating_detail);
                barRating = findViewById(R.id.bar_rating);
                tvReviewer = findViewById(R.id.reviewer_detail);

                tvRating.setText(String.valueOf(selectTV.getVoteAverage()));
                tvReviewer.setText(String.valueOf(selectTV.getVoteCount()));

                double bar = selectTV.getVoteAverage() * 10;
                barRating.setRating((float) ((bar * 5) / 100));
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getGenres(final TVShow tvs) {
        data.getGenre(new TVShowData.OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (tvs.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : tvs.getGenres()) {
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
        Toast.makeText(DetailActivityTVShow.this, "Failed to load data", Toast.LENGTH_SHORT).show();
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
