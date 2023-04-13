package com.example.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapp.repository.MovieRepository
import com.example.myapp.room.MovieDatabase

class MainViewModelFactory(private val movieDatabase: MovieDatabase, private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(movieDatabase,repository) as T
    }
}