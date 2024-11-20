package roomDB.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import roomDB.model.SmsSaver

@Dao
interface smsDao {
    @Query("Select * FROM SMS_Database ORDER BY Date&Time ASC")
    fun getAllSMS(): Flow<List<SmsSaver>>

    @Insert
    fun insetSMS(sms: SmsSaver)

    @Delete
    fun deleteSMS(sms:SmsSaver)
}