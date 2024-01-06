package com.example.apparchitecturedao.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apparchitecturedao.database.SleepDatabaseDao
import com.example.apparchitecturedao.database.SleepNight
import kotlinx.coroutines.launch

class SleepDetailViewModel(sleepId: Long, dataSource: SleepDatabaseDao) : ViewModel() {
    private var _night = MutableLiveData<SleepNight?>()
    val night: LiveData<SleepNight?> = _night
    private var _navigateBack  = MutableLiveData<Boolean?>()
    val navigateBack: LiveData<Boolean?> = _navigateBack

    init {
        viewModelScope.launch {
            getSleepNight(sleepId, dataSource)
        }
    }

    fun onClose(){
        _navigateBack.value = true
    }

    fun onNavigate(){
        _navigateBack.value = null
    }

    private suspend fun getSleepNight(nightId: Long, dataSource: SleepDatabaseDao) {
        _night.value = dataSource.get(nightId)
    }
}