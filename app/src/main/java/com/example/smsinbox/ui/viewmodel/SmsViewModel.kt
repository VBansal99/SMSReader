package com.example.smsinbox.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.smsinbox.roomDB.Dao.smsRepository
import com.example.smsinbox.roomDB.model.SmsSaver

class SmsViewModel(private val repository: smsRepository) : ViewModel() {

    val allSms: LiveData<List<SmsSaver>> = repository.allSMS.asLiveData()

    fun insertSms(sms: SmsSaver) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(sms)
        }
    }

    fun deleteSms(sms: SmsSaver) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(sms)
        }
    }
}

class SmsViewModelFactory(private val repository: smsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SmsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SmsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
