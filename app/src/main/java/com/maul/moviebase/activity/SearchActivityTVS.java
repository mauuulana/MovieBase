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
import com.maul.moviebase.adapter.TVShowCardAdapter;
import com.maul.moviebase.modal.TVShow;
import com.maul.moviebase.modal.TVShowData;
import com.maul.moviebase.utils.ItemClick;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class SearchActivityTVS extends AppCompatActivity implements View.OnClickListener {
    public static String SEARCH_TVS = "query";
    public static final String EXTRA_SEARCH = "extra_search";

    private RecyclerView rvSearchTVS;
    private TVShowCardAdapter adapterTVS;
    private TVShowData tvsData;
    private AVLoadingIndicatorView loading;
    private TextView result;
    private ArrayList<TVShow> listTVs = new ArrayList<>();


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        rvSearchTVS = findViewById(R.id.rv_result);
        loading = findViewById(R.id.loadingResult);
        loading.setIndicator("LineScalePulseOutIndicator");
        tvsData = TVShowData.getInstance();
        ImageView btnBackResult = findViewById(R.id.back_result);
        btnBackResult.setOnClickListener(this);
        result = findViewById(R.id.tv_result);

        String titleActivityResultTV = getIntent().getStringExtra(EXTRA_SEARCH);
        TextView titleActivity = findViewById(R.id.title_activity_result);
        titleActivity.setText(titleActivityResultTV);

        Intent intent = getIntent();
        String query = intent.getStringExtra(SEARCH_TVS);

        showRV();
        getSearchTV(query);

    }

    private void showRV() {
        adapterTVS = new TVShowCardAdapter(this);
        rvSearchTVS.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSearchTVS.setHasFixedSize(true);
    }

    private void getSearchTV(String query) {
        tvsData.getSearchTv(query, new TVShowData.OnGetSearchTVShow() {
            @Override
            public void onSuccess(ArrayList<TVShow> tvs) {
                loading.setVisibility(View.GONE);
                adapterTVS.setData(tvs);
                rvSearchTVS.setAdapter(adapterTVS);
                result.setText(query);
                listTVs.addAll(tvs);
                ItemClick.addTo(rvSearchTVS).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(SearchActivityTVS.this, DetailActivityTVShow.class);
                    intent.putExtra(DetailActivityTVShow.EXTRA_TVS_CATEGORIES, "TV Shows");
                    intent.putExtra(DetailActivityTVShow.EXTRA_TVS, listTVs.get(position));
                    SearchActivityTVS.this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
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
