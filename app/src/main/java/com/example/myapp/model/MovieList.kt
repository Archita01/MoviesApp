package com.example.myapp.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class MovieList (
  @SerializedName("Title") open        var Title    :       String? = null,
  @SerializedName("Year")  open      var Year     :       String? = null,
  @SerializedName("Summary")  open      var Summary  :       String? = null,
  @SerializedName("Short Summary")  open var      Short_Summary : String? = null,
  @SerializedName("Genres") open       var Genres   :       String? = null,
  @PrimaryKey
  @SerializedName("IMDBID")      @NonNull  open var IMDBID   :       String,
  @SerializedName("Runtime")     open   var Runtime  :       String? = null,
  @SerializedName("YouTube Trailer")  open var      YouTube_Trailer : String? = null,
  @SerializedName("Rating")   open     var Rating   :       String? = null,
  @SerializedName("Movie Poster") open  var      Movie_Poster  : String? = null,
  @SerializedName("Director") open       var Director :       String? = null,
  @SerializedName("Writers")   open     var Writers  :       String? = null,
  @SerializedName("Cast") open       var Cast     :       String? = null,
  var isFavourite: Boolean = false

)