package com.mordeccai.quickcam.data

import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.content.Context
import com.mordeccai.quickcam.data.models.Product
import com.mordeccai.quickcam.data.models.ProductDao


@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "quickCam")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE as AppDatabase
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}