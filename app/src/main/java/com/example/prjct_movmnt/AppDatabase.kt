package com.example.prjct_movmnt.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovementReading::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao
}