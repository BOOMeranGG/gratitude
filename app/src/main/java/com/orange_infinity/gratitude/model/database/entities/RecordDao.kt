package com.orange_infinity.gratitude.model.database.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cashCheque: Record)

    @Query("SELECT * FROM Record ORDER BY id ASC")
    fun findAll(): List<Record>
}