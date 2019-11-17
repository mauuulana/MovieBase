package com.maul.moviebase.modal;

import androidx.annotation.NonNull;

import com.maul.moviebase.BuildConfig;
import com.maul.moviebase.api.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TVShowData {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private static TVShowData data;
    private ApiInterface api;

    private TVShowData(ApiInterface api) {
        this.api = api;
    }

    public static TVShowData getInstance() {
        if (data == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            data = new TVShowData(retrofit.create(ApiInterface.class));
        }

        return data;
    }

    public void getUpcomingTV(final OnGetTVShowCallback callback) {
        String apiKey = BuildConfig.API_KEY;
        api.getUpcomingTv(apiKey, LANGUAGE, 1)
                .enqueue(new Callback<TVShowResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TVShowResponse> call, @NonNull Response<TVShowResponse> response) {
                        if (response.isSuccessful()) {
                            TVShowResponse TvShowResponse = response.body();
                            if (TvShowResponse != null && TvShowResponse.getTvs() != null) {
                                callback.onSuccess(TvShowResponse.getTvs());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TVShowResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getGenre(final OnGetGenresCallback callback) {
        api.getGenres(BuildConfig.API_KEY, LANGUAGE)
                .enqueue(new Callback<GenreResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GenreResponse> call, @NonNull Response<GenreResponse> response) {
                        if (response.isSuccessful()) {
                            GenreResponse genreResponse = response.body();
                            if (genreResponse != null && genreResponse.getGenres() != null) {
                                callback.onSuccess(genreResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GenreResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getSearchTv(String query, final OnGetSearchTVShow callback) {
        String apiKey = BuildConfig.API_KEY;
        api.getSearchTv(query, apiKey)
                .enqueue(new Callback<TVShowResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TVShowResponse> call, @NonNull Response<TVShowResponse> response) {
                        if (response.isSuccessful()) {
                            TVShowResponse tvsResponse = response.body();
                            if (tvsResponse != null && tvsResponse.getTvs() != null) {
                                callback.onSuccess(tvsResponse.getTvs());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TVShowResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getTV(int tvId, final OnGetDetailTVShow callback) {
        api.getTVs(tvId, BuildConfig.API_KEY, LANGUAGE)
                .enqueue(new Callback<TVShow>() {
                    @Override
                    public void onResponse(@NonNull Call<TVShow> call, @NonNull Response<TVShow> response) {
                        if (response.isSuccessful()) {
                            TVShow tv = response.body();
                            if (tv != null) {
                                callback.onSuccess(tv);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TVShow> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getPopularTV(int page, final OnGetPageTVShow callback) {
        api.getPopularTv(BuildConfig.API_KEY, LANGUAGE, page)
                .enqueue(new Callback<TVShowResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TVShowResponse> call, @NonNull Response<TVShowResponse> response) {
                        if (response.isSuccessful()) {
                            TVShowResponse tvShowResponse = response.body();
                            if (tvShowResponse != null && tvShowResponse.getTvs() != null) {
                                callback.onSuccess(tvShowResponse.getPage(), tvShowResponse.getTvs());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TVShowResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getUpcomingTVMore(int page, final OnGetPageTVShow callback) {
        api.getUpcomingTv(BuildConfig.API_KEY, LANGUAGE, page)
                .enqueue(new Callback<TVShowResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TVShowResponse> call, @NonNull Response<TVShowResponse> response) {
                        if (response.isSuccessful()) {
                            TVShowResponse tvShowResponse = response.body();
                            if (tvShowResponse != null && tvShowResponse.getTvs() != null) {
                                callback.onSuccess(tvShowResponse.getPage(), tvShowResponse.getTvs());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TVShowResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public interface OnGetDetailTVShow {
        void onSuccess(TVShow tv);
        void onError();
    }

    public interface OnGetGenresCallback {
        void onSuccess(List<Genre> genre);
        void onError();
    }

    public interface OnGetPageTVShow {
        void onSuccess(int page, ArrayList<TVShow> tvShow);
        void onError();
    }

    public interface OnGetTVShowCallback {
        void onSuccess(final ArrayList<TVShow> tvShow);
        void onError();
    }

    public interface OnGetSearchTVShow {
        void onSuccess(ArrayList<TVShow> tvs);
        void onError();
    }

}
