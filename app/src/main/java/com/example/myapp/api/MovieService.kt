package com.example.myapp.api

import com.example.myapp.model.Movie
import retrofit2.Response
import retrofit2.http.GET

interface MovieService {

    @GET("/1.json")
    suspend fun getMovies() : Response<Movie>

    @GET("/2.json")
    suspend fun getMovies2() : Response<Movie>

}