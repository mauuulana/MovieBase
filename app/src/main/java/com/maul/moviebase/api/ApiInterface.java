package com.maul.moviebase.api;

import com.maul.moviebase.modal.GenreResponse;
import com.maul.moviebase.modal.Movie;
import com.maul.moviebase.modal.MovieResponse;
import com.maul.moviebase.modal.TVShow;
import com.maul.moviebase.modal.TVShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("discover/movie")
    Call<MovieResponse> getReleasedMovies(
            @Query("api_key") String apiKey,
            @Query("primary_release_date.gte") String gte,
            @Query("primary_release_date.lte") String lte
    );

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<MovieResponse> getSearchMovie(
            @Query("query") String query,
            @Query("api_key") String apiKey
    );

    //search_tv
    @GET("search/tv")
    Call<TVShowResponse> getSearchTv(
            @Query("query") String query,
            @Query("api_key") String apiKey
    );

    @GET("tv/popular")
    Call<TVShowResponse> getPopularTv(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/on_the_air")
    Call<TVShowResponse> getUpcomingTv(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @GET("tv/{tv_id}")
    Call<TVShow> getTVs(
            @Path("tv_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );
}
