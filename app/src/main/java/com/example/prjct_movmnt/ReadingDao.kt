package com.example.prjct_movmnt.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import com.example.prjct_movmnt.data.MovementReading

@Dao
interface ReadingDao {
    @Insert suspend fun insert(reading: MovementReading)
    @Query("SELECT * FROM readings ORDER BY timestamp ASC")
    suspend fun getAll(): List<MovementReading>
    @Query ("DELETE FROM readings")
    suspend fun clearAll()
}