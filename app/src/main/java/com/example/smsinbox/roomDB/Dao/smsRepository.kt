package com.example.smsinbox.roomDB.Dao

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import com.example.smsinbox.roomDB.model.SmsSaver

class smsRepository(private val sms_dao: smsDao) {
    val allSMS: Flow<List<SmsSaver>> = sms_dao.getAllSMS()

    @WorkerThread
    suspend fun insert(smsInsert: SmsSaver) {
        sms_dao.insetSMS(smsInsert)
    }

    @WorkerThread
    suspend fun delete(smsDelete: SmsSaver) {
        sms_dao.deleteSMS(smsDelete)
    }
}