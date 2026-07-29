package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.VedvoraDao
import com.example.data.entity.ActivityLogEntity
import com.example.data.entity.BookingEntity
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.NoticeEntity
import com.example.data.entity.VisitorEntity

@Database(
    entities = [
        VisitorEntity::class,
        InvoiceEntity::class,
        BookingEntity::class,
        NoticeEntity::class,
        ActivityLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vedvoraDao(): VedvoraDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vedvora_residency.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
