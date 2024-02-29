package br.goldbach.activities.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.goldbach.activities.data.dao.ActivitiesDao
import br.goldbach.activities.data.model.Activity

@Database(entities = [Activity::class], version = 1, exportSchema = false)
abstract class ActivitiesDatabase : RoomDatabase() {

    abstract fun ActivitiesDao(): ActivitiesDao

    companion object {
        @Volatile
        private var Instance: ActivitiesDatabase? = null

        fun getDatabase(context: Context) : ActivitiesDatabase {
            return Instance?: synchronized(this) {
                Room.databaseBuilder(context, ActivitiesDatabase::class.java, "acitivies_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}