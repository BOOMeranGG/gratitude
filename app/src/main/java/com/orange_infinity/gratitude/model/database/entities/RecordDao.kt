package com.orange_infinity.gratitude.model.database.entities

import androidx.room.*

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: Record)

    @Query("SELECT * FROM Record ORDER BY id DESC")
    fun findAll(): List<Record>

    @Query("SELECT * FROM Record ORDER BY id DESC LIMIT 15")
    fun findAllWithLimit(): List<Record>

    @Query("SELECT COUNT(*) FROM Record")
    fun getCountOfRecords(): Int
}