package com.example.myapp.viewmodel


import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.model.Movie
import com.example.myapp.model.MovieList
import com.example.myapp.repository.MovieRepository
import com.example.myapp.room.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val movieDatabase: MovieDatabase, private val repository: MovieRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO){
            repository.getMovies()
        }

    }

    suspend fun updateMovie(movie: MovieList) {
        withContext(Dispatchers.IO) {

                try {
                    movieDatabase.movieDao().updateMovie(movie)
                    val mov = movieDatabase.movieDao().getMovie()
                    movieLiveData.value = Movie(mov)
                    Log.d("TAG", "Movie updated successfully")

                } catch (e: Exception) {
                    Log.e("TAG", "Error updating movie: ${e.message}")
                }

        }

    }

    suspend fun deleteItem(movieList: MovieList) {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteMovie(movieList)

        }

    }

    val items = MutableLiveData<SnapshotStateList<MovieList>>()

    suspend fun removeItem(item: MovieList) {
        items.value?.remove(item)
    }
    private val movieLiveData = MutableLiveData<Movie>()

    val movies : LiveData<Movie>
        get() = repository.movies
}