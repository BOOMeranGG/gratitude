package com.orange_infinity.gratitude.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.orange_infinity.gratitude.model.database.entities.Record
import com.orange_infinity.gratitude.model.database.entities.RecordDao

@Database(entities = [Record::class], version = 6)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRecordDao(): RecordDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "app-database.db")
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}