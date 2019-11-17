package com.maul.moviebase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maul.moviebase.R;
import com.maul.moviebase.activity.DetailActivityMovie;
import com.maul.moviebase.activity.DetailActivityTVShow;
import com.maul.moviebase.activity.MoreUpcomingMovie;
import com.maul.moviebase.activity.MoreUpcomingTV;
import com.maul.moviebase.activity.SettingActivity;
import com.maul.moviebase.adapter.MovieThumbnailAdapter;
import com.maul.moviebase.adapter.TVShowThumbnailAdapter;
import com.maul.moviebase.modal.Movie;
import com.maul.moviebase.modal.MovieData;
import com.maul.moviebase.modal.TVShow;
import com.maul.moviebase.modal.TVShowData;

import com.maul.moviebase.utils.ItemClick;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private int[] sampleBanner = {R.drawable.banner1, R.drawable.banner2};
    private AVLoadingIndicatorView aviLoadingMovie, aviLoadingTV;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private ArrayList<TVShow> listTvs = new ArrayList<>();
    private MovieThumbnailAdapter movieThumbAdapt;
    private TVShowThumbnailAdapter tvsThumbAdapt;
    private RecyclerView rvMovie, rvTVShow;
    private MovieData mData;
    private TVShowData tData;

    private final static String LIST_UPCOMING_MOVIE = "list_movie";
    private final static String LIST_UPCOMING_TVSHOW = "list_tvshow";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        CarouselView carouselView = view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleBanner.length);
        carouselView.setImageListener(imageListener);

        aviLoadingMovie= view.findViewById(R.id.loadingMovie);
        aviLoadingTV= view.findViewById(R.id.loadingTv);
        aviLoadingMovie.setIndicator("CubeTransitionIndicator");
        aviLoadingTV.setIndicator("CubeTransitionIndicator");

        ImageButton btn_setting = view.findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(this);

        TextView mMore = view.findViewById(R.id.upcoming_movie);
        TextView tMore = view.findViewById(R.id.upcoming_tv);

        mMore.setOnClickListener(this);
        tMore.setOnClickListener(this);

        rvMovie = view.findViewById(R.id.rv_upcoming_movie);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvMovie.setHasFixedSize(true);
        mData = MovieData.getInstance();

        rvTVShow = view.findViewById(R.id.rv_upcoming_tv);
        rvTVShow.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTVShow.setHasFixedSize(true);
        tData = TVShowData.getInstance();


        if (savedInstanceState != null) {
            aviLoadingMovie.setVisibility(INVISIBLE);
            final ArrayList<Movie> listMovie = savedInstanceState.getParcelableArrayList(LIST_UPCOMING_MOVIE);
            assert listMovie != null;
            listMovies.addAll(listMovie);
            movieThumbAdapt = new MovieThumbnailAdapter(getActivity());
            movieThumbAdapt.setData(listMovie);
            rvMovie.setAdapter(movieThumbAdapt);

            aviLoadingTV.setVisibility(INVISIBLE);
            final ArrayList<TVShow> listTV = savedInstanceState.getParcelableArrayList(LIST_UPCOMING_TVSHOW);
            assert listTV != null;
            listTvs.addAll(listTV);
            tvsThumbAdapt = new TVShowThumbnailAdapter(getActivity());
            tvsThumbAdapt.setData(listTV);
            rvTVShow.setAdapter(tvsThumbAdapt);

            ItemClick.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailActivityMovie.class);
                intent.putExtra(DetailActivityMovie.EXTRA_MOVIES_CATEGORIES, "Movies");
                intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, listMovie.get(position));
                (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                startActivity(intent);
            });

            ItemClick.addTo(rvTVShow).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailActivityTVShow.class);
                intent.putExtra(DetailActivityTVShow.EXTRA_TVS_CATEGORIES, "TV Shows");
                intent.putExtra(DetailActivityTVShow.EXTRA_TVS, listTV.get(position));
                (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                startActivity(intent);
            });
        } else {
            getData();

        }

        return  view;
    }

    private ImageListener imageListener = (position, imageView) -> imageView.setImageResource(sampleBanner[position]);

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_UPCOMING_MOVIE, listMovies);
        outState.putParcelableArrayList(LIST_UPCOMING_TVSHOW, listTvs);
    }

    private void getData() {
        mData.getMovieUpcoming(new MovieData.OnGetMoviesCallback() {

            @Override
            public void onSuccess(final ArrayList<Movie> movies) {
                movieThumbAdapt = new MovieThumbnailAdapter(getContext());
                movieThumbAdapt.setData(movies);
                rvMovie.setAdapter(movieThumbAdapt);
                aviLoadingMovie.setVisibility(GONE);
                listMovies.addAll(movies);

                ItemClick.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(getActivity(), DetailActivityMovie.class);
                    intent.putExtra(DetailActivityMovie.EXTRA_MOVIES_CATEGORIES, "Movies");
                    intent.putExtra(DetailActivityMovie.EXTRA_MOVIES, movies.get(position));
                    (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                    startActivity(intent);
                });
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.failure);
                Toast.makeText(getActivity(), toast_msg, Toast.LENGTH_SHORT).show();
            }
        });
        tData.getUpcomingTV(new TVShowData.OnGetTVShowCallback() {
            @Override
            public void onSuccess(final ArrayList<TVShow> tvs) {
                tvsThumbAdapt = new TVShowThumbnailAdapter(getActivity());
                tvsThumbAdapt.setData(tvs);
                rvTVShow.setAdapter(tvsThumbAdapt);
                aviLoadingTV.setVisibility(GONE);
                listTvs.addAll(tvs);

                ItemClick.addTo(rvTVShow).setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intent = new Intent(getActivity(), DetailActivityTVShow.class);
                    intent.putExtra(DetailActivityTVShow.EXTRA_TVS_CATEGORIES, "TV Shows");
                    intent.putExtra(DetailActivityTVShow.EXTRA_TVS, tvs.get(position));
                    (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                    startActivity(intent);
                });
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.failure);
                Toast.makeText(getActivity(), toast_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                Intent mIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(mIntent);
                (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                break;

            case R.id.upcoming_movie:
                Intent moreMovieIntent = new Intent(getActivity(), MoreUpcomingMovie.class);
                moreMovieIntent.putExtra(MoreUpcomingMovie.EXTRA_TITLE, "Upcoming Movies");
                startActivity(moreMovieIntent);
                (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                break;

            case R.id.upcoming_tv:
                Intent moreTVIntent = new Intent(getActivity(), MoreUpcomingTV.class);
                moreTVIntent.putExtra(MoreUpcomingTV.EXTRA_TITLE, "On The Air TV Shows");
                startActivity(moreTVIntent);
                (Objects.requireNonNull(getActivity())).overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                break;
        }
    }
}
