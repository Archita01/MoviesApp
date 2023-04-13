package com.example.myapp.model

import com.google.gson.annotations.SerializedName

data class Movie (

    @SerializedName("Movie List")  var Movie_List : List<MovieList>

)