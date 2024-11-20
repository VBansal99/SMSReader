package ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import roomDB.Dao.smsRepository
import roomDB.model.SmsSaver

class SmsViewModel(private val repository: smsRepository) : ViewModel() {

    // LiveData for all SMS
    val allSms: LiveData<List<SmsSaver>> = repository.allSMS.asLiveData()

    // Function to insert SMS into the database
    fun insertSms(sms: SmsSaver) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(sms)
        }
    }

    // Function to delete SMS from the database
    fun deleteSms(sms: SmsSaver) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(sms)
        }
    }
}

// ViewModel Factory for Dependency Injection
class SmsViewModelFactory(private val repository: smsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SmsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SmsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}