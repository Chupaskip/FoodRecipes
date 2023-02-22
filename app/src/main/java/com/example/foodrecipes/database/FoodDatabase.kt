package com.example.foodrecipes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodrecipes.models.Meal

@Database(
    entities = [Meal::class],
    version = 2
)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun getFoodDao(): FoodDao

    companion object {
        @Volatile
        private var instance: FoodDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FoodDatabase::class.java,
                "food_db.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}