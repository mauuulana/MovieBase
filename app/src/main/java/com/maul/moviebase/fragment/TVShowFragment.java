package com.maul.moviebase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maul.moviebase.R;
import com.maul.moviebase.activity.DetailActivityTVShow;
import com.maul.moviebase.activity.SearchActivityTVS;
import com.maul.moviebase.adapter.TVShowCardAdapter;
import com.maul.moviebase.modal.TVShow;
import com.maul.moviebase.modal.TVShowData;
import com.maul.moviebase.utils.ItemClick;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Objects;

import static com.maul.moviebase.activity.SearchActivityTVS.SEARCH_TVS;

public class TVShowFragment extends Fragment implements View.OnClickListener {

    private final static String LIST_STATE_KEY = "STATE";
    private ArrayList<TVShow> list = new ArrayList<>();
    private TVShowData data;
    private RecyclerView rv_discover_tv;
    private boolean isFetchingTV;
    private int currentPage = 1;
    private TVShowCardAdapter adapter;
    private AVLoadingIndicatorView loading;
    private final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


    public TVShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = TVShowData.getInstance();

        TextView btnMore = view.findViewById(R.id.more_btn_tvs);
        btnMore.setOnClickListener(this);

        loading = view.findViewById(R.id.loadingTVShow);
        loading.setIndicator("LineScalePulseOutIndicator");

        rv_discover_tv = view.findViewById(R.id.rv_popular_tvshows);

        SearchView searchView = view.findViewById(R.id.search_tvs);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getActivity(), SearchActivityTVS.class);
                intent.putExtra(SearchActivityTVS.EXTRA_SEARCH, "TV Show Results");
                intent.putExtra(SEARCH_TVS, query);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        if (savedInstanceState != null) {
            loading.setVisibility(View.INVISIBLE);
            final ArrayList<TVShow> tvShows = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY);
            assert tvShows != null;
            list.addAll(tvShows);
            adapter = new TVShowCardAdapter(getActivity());
            adapter.setData(tvShows);
            rv_discover_tv.setAdapter(adapter);
            rv_discover_tv.setLayoutManager(layoutManager);

            ItemClick.addTo(rv_discover_tv).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailActivityTVShow.class);
                intent.putExtra(DetailActivityTVShow.EXTRA_TVS_CATEGORIES, "TV Shows");
                intent.putExtra(DetailActivityTVShow.EXTRA_TVS, tvShows.get(position));
                (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                startActivity(intent);
            });

        } else {
            getTVShow(1);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY, list);
    }


    private void getTVShow(int page) {
        isFetchingTV = true;
        data.getPopularTV(page, new TVShowData.OnGetPageTVShow() {

            @Override
            public void onSuccess(int page, ArrayList<TVShow> tvShows) {
                Log.d("TV Show Data", "Current Page = " + currentPage);
                loading.setVisibility(View.INVISIBLE);
                if (adapter == null) {
                    adapter = new TVShowCardAdapter(getContext());
                    adapter.setData(tvShows);
                    list.addAll(tvShows);
                    rv_discover_tv.setAdapter(adapter);
                    rv_discover_tv.setLayoutManager(layoutManager);

                    ItemClick.addTo(rv_discover_tv).setOnItemClickListener((recyclerView, position, v) -> {
                        Intent intent = new Intent(getActivity(), DetailActivityTVShow.class);
                        intent.putExtra(DetailActivityTVShow.EXTRA_TVS_CATEGORIES, "TV Shows");
                        intent.putExtra(DetailActivityTVShow.EXTRA_TVS, tvShows.get(position));
                        (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                        startActivity(intent);
                    });
                } else {
                    isFetchingTV = false;
                    adapter.setData(tvShows);
                }
                currentPage = page;
                isFetchingTV = false;
            }

            @Override
            public void onError() {

            }

        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.more_btn_tvs) {
            if (!isFetchingTV) {
                loading.setVisibility(View.VISIBLE);
                getTVShow(currentPage + 1);
            }
        }
    }
}
