package com.example.myapp.room

import androidx.room.*
import com.example.myapp.model.MovieList

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: List<MovieList>)

    @Query("SELECT * FROM movies")
    fun getMovie() : List<MovieList>

    @Update
    fun updateMovie(movie: MovieList)

     @Query("SELECT * FROM movies WHERE isFavourite = 1")
     fun getFavouriteMovies(): List<MovieList>

    @Query("DELETE FROM movies WHERE IMDBID = :id")
    fun delete(id : String?):Int

   // @Delete
    //suspend fun deleteStudent(movieList: MovieList)
}