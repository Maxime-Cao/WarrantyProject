package com.warranty.warrantyproject.infrastructures.db;

import android.content.Context
import androidx.room.Database;
import androidx.room.Room
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters

@Database(entities = [WarrantyEntity::class],version = 3,exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class WarrantyDatabase : RoomDatabase() {

    abstract fun warrantyDao(): WarrantyDao

    companion object {
        @Volatile
        private var INSTANCE : WarrantyDatabase? = null

        fun getDatabase(context: Context): WarrantyDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WarrantyDatabase::class.java,
                        "warranty_data_database"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}