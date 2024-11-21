package com.example.smsinbox.roomDB.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class SmsReader(
    val info:String,
    val date: String,
    val time: String,
    val title:String,
    val sender:String,
    val smsDescription: String
)

@Entity(tableName = "SMS_Database")
data class SmsSaver(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "Info") val info: String,
    @ColumnInfo(name = "Date") val date: String,
    @ColumnInfo(name = "Time") val time: String,
    @ColumnInfo(name = "Sender") val sender: String,
    @ColumnInfo(name = "SMS_description") val smsDescription: String
)
