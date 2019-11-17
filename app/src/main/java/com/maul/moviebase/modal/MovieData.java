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

public class MovieData {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";
    private static MovieData data;
    private ApiInterface api;

    private MovieData(ApiInterface api) {
        this.api = api;
    }

    public static MovieData getInstance() {
        if (data == null) {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            data = new MovieData(retrofit.create(ApiInterface.class));
        }
        return data;
    }

    public void getMovieUpcoming(final OnGetMoviesCallback callback) {
        String apiKey = BuildConfig.API_KEY;
        api.getUpcomingMovies(apiKey, LANGUAGE, 1)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
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

    public void getSearchMovie(String query, final OnGetSearchMovie callback) {
        String apiKey = BuildConfig.API_KEY;
        api.getSearchMovie(query, apiKey)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMovie(int movieId, final OnGetDetailMovie callback) {
        api.getMovie(movieId, BuildConfig.API_KEY, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getPopularMovie(int page, final OnGetPageMovie callback) {
        api.getPopularMovies(BuildConfig.API_KEY, LANGUAGE, page)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMovieUpcomingMore(int page, final OnGetPageMovie callback) {
        api.getUpcomingMovies(BuildConfig.API_KEY, LANGUAGE, page)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getReleased(String release, String release1, final OnGetReleasedMovie callback) {
        String apiKey = BuildConfig.API_KEY;
        api.getReleasedMovies(apiKey, release, release1)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public interface OnGetDetailMovie {
        void onSuccess(Movie movie);
        void onError();
    }

    public interface OnGetGenresCallback {
        void onSuccess(List<Genre> genre);
        void onError();
    }

    public interface OnGetMoviesCallback {
        void onSuccess(final ArrayList<Movie> movies);
        void onError();
    }

    public interface OnGetPageMovie {
        void onSuccess(int page, ArrayList<Movie> movies);
        void onError();
    }

    public interface OnGetSearchMovie {
        void onSuccess(ArrayList<Movie> movies);
        void onError();
    }

    public interface OnGetReleasedMovie {
        void onSuccess(ArrayList<Movie> movies);
        void onError();
    }
}


