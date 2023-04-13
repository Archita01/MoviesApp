package com.example.myapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapp.model.MovieList

@Database(entities = [MovieList::class] ,version = 2)
abstract class MovieDatabase : RoomDatabase() {


    abstract fun movieDao() : RoomDao
    companion object {

        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        MovieDatabase::class.java,
                        "moviesDB"
                    ).build()
                }
            }
            return INSTANCE!!
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE movies ADD COLUMN isFavourite INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}