package com.example.smsinbox.roomDB.Dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smsinbox.roomDB.model.SmsSaver

@Database(entities = [SmsSaver::class], version = 1, exportSchema = false)
abstract class smsRoomDatabase : RoomDatabase() {
    abstract fun sms_dao(): smsDao

    companion object {
        private var INSTANCE: smsRoomDatabase? = null
        fun getDatabase(context: Context): smsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    smsRoomDatabase::class.java,
                    "sms_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}