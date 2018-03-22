package com.reshmi.james.popularmovies.rest;

import com.reshmi.james.popularmovies.model.MoviesResponse;
import com.reshmi.james.popularmovies.model.ReviewResponse;
import com.reshmi.james.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RestEndpointInterface {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<MoviesResponse> getMovieDetails(@Path("movie_id") String movieId);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") long id, @Query("api_key") String apiKey);


}
