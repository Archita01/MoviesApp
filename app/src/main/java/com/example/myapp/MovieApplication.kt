package com.example.myapp

import android.app.Application
import com.example.myapp.repository.MovieRepository

class MovieApplication : Application() {

    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
       // val movieService = RetrofitHelper.getInstance().create(MovieService::class.java)
        //val database = MovieDatabase.getDatabase(applicationContext)
        //movieRepository = MovieRepository(movieService, database, applicationContext)
    }
}
