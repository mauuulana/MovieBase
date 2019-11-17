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
import com.maul.moviebase.adapter.TVShowCardAdapter;
import com.maul.moviebase.modal.TVShow;
import com.maul.moviebase.modal.TVShowData;
import com.maul.moviebase.utils.ItemClick;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static com.maul.moviebase.R.id.back_more;
import static com.maul.moviebase.activity.SearchActivityMore.SEARCH_MORE_TV;

public class MoreUpcomingTV extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TITLE = "extra_title";
    private final static String LIST_STATE_KEY = "STATE";
    private ArrayList<TVShow> listTVUpcoming = new ArrayList<>();
    private TVShowData data;
    private int currentPage = 2;
    private boolean isFetchingTVSMore;
    private RecyclerView rvMoreTV;
    private TVShowCardAdapter adapter;
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

        rvMoreTV = findViewById(R.id.rv_more);
        rvMoreTV.setLayoutManager(new LinearLayoutManager(getParent(), LinearLayoutManager.VERTICAL, false));
        rvMoreTV.setHasFixedSize(true);
        data = TVShowData.getInstance();

        SearchView searchView = findViewById(R.id.search_more);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MoreUpcomingTV.this, SearchActivityMore.class);
                intent.putExtra(SearchActivityMore.EXTRA_SEARCH_TVSHOW, "TV Shows Results");
                intent.putExtra(SEARCH_MORE_TV, query);
                MoreUpcomingTV.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
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
            final ArrayList<TVShow> listTV = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
            assert listTV != null;
            listTVUpcoming.addAll(listTV);
            adapter = new TVShowCardAdapter(getParent());
            adapter.setData(listTV);
            rvMoreTV.setAdapter(adapter);

            ItemClick.addTo(rvMoreTV).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(MoreUpcomingTV.this, DetailActivityTVShow.class);
                intent.putExtra(DetailActivityTVShow.EXTRA_TVS_CATEGORIES, "TV Shows");
                intent.putExtra(DetailActivityTVShow.EXTRA_TVS, listTV.get(position));
                MoreUpcomingTV.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                startActivity(intent);
            });

        } else {
            getData(2);

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY, listTVUpcoming);
    }

    private void getData(int page) {
        isFetchingTVSMore = true;
        data.getUpcomingTVMore(page, new TVShowData.OnGetPageTVShow() {
            @Override
            public void onSuccess(int page, ArrayList<TVShow> tvShows) {
                loading.setVisibility(View.INVISIBLE);
                Log.d("TV Show Data", "Current Page = " + currentPage);

                if (adapter == null) {
                    adapter = new TVShowCardAdapter(getBaseContext());
                    adapter.setData(tvShows);
                    rvMoreTV.setAdapter(adapter);
                    loading.setVisibility(GONE);
                    listTVUpcoming.addAll(tvShows);

                    ItemClick.addTo(rvMoreTV).setOnItemClickListener((recyclerView, position, v) -> {
                        Intent intent = new Intent(MoreUpcomingTV.this, DetailActivityTVShow.class);
                        intent.putExtra(DetailActivityTVShow.EXTRA_TVS_CATEGORIES, "TV Shows");
                        intent.putExtra(DetailActivityTVShow.EXTRA_TVS, tvShows.get(position));
                        MoreUpcomingTV.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                        startActivity(intent);
                    });
                } else {
                    isFetchingTVSMore = false;
                    adapter.setData(tvShows);
                }
                currentPage = page;
                isFetchingTVSMore = false;
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
            if (!isFetchingTVSMore) {
                loading.setVisibility(View.VISIBLE);
                getData(currentPage + 1);
            }
        }
    }
}
