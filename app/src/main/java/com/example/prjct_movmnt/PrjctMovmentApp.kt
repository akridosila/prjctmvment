package com.example.prjct_movmnt

import android.app.Application
import androidx.room.Room
import com.example.prjct_movmnt.data.AppDatabase

class PrjctMovmntApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "movement-db"
        ).build()
    }
}