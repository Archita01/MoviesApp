package com.example.myapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapp.api.MovieService
import com.example.myapp.model.Movie
import com.example.myapp.model.MovieList
import com.example.myapp.room.MovieDatabase
import com.example.myapp.utils.NetworkUtils


class MovieRepository(private val apiInterface : MovieService, private val movieDatabase : MovieDatabase,  private val applicationContext: Context) {

    private val movieLiveData = MutableLiveData<Movie>()

    val movies : LiveData<Movie>
        get() = movieLiveData

    suspend fun getMovies(){
        if(NetworkUtils.isInternetAvailable(applicationContext)) {
            Log.e("Hello","Internet available!")
            var result = apiInterface.getMovies()
            var result2 = apiInterface.getMovies2()
            if (result?.body() != null) {
                Log.e("Result",result?.body().toString())
                var list: List<MovieList> = listOf<MovieList>(MovieList("2","2","2","2","2","2","2","2","2","2","2","2","2"))

                result.body()!!.Movie_List = merge(result?.body()!!.Movie_List, list)
                result.body()!!.Movie_List = merge(result?.body()!!.Movie_List, result2?.body()!!.Movie_List)
                movieDatabase.movieDao().insertMovie(result.body()!!.Movie_List)
                movieLiveData.postValue(result.body())
            }

        }
        else{
            Log.e("Hello","No Internet available!")
            val mov = movieDatabase.movieDao().getMovie()
            val movieList = Movie(mov)
            movieLiveData.postValue(movieList)
        }

    }

    suspend fun deleteMovie(movieList: MovieList) {
        println("${movieList.IMDBID}")
        val rowsDeleted = movieDatabase.movieDao().delete(movieList.IMDBID)
        val mov = movieDatabase.movieDao().getMovie()
        val movieList = Movie(mov)
        movieLiveData.postValue(movieList)
        Log.e("INSERT",movieList.toString())
        if (rowsDeleted > 0) {
            Log.e("H","Successfully deleted!")
        } else {
            Log.e("H","Not Successful :( deleted!")
        }
    }
    suspend fun delete(movieList: MovieList) {


    }
    fun <T> merge(first: List<T>, second: List<T>): List<T> {
        return first + second
    }
    suspend fun getMovies2(){
        if(NetworkUtils.isInternetAvailable(applicationContext)) {
            Log.e("Hello","Internet available!")
            val result = apiInterface.getMovies2()
            if (result?.body() != null) {
                Log.e("Result ",result?.body().toString())
                movieDatabase.movieDao().insertMovie(result.body()!!.Movie_List)
                movieLiveData.postValue(result.body())
            }
        }
        else{
            Log.e("Hello","No Internet available!")
            val mov = movieDatabase.movieDao().getMovie()
            val movieList = Movie(mov)
            movieLiveData.postValue(movieList)
        }

    }


}