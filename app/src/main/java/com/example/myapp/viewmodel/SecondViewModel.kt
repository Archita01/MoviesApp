package com.example.myapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.model.Movie
import com.example.myapp.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondViewModel(private val repository: MovieRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO){
            repository.getMovies2()
        }
    }

    val movies : LiveData<Movie>
        get() = repository.movies
}