package com.example.apparchitecturedao.sleepdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apparchitecturedao.database.SleepDatabaseDao

class SleepDetailViewModelFactory(
    private val sleepId: Long,
    private val dataSource: SleepDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepDetailViewModel::class.java)) {
            return SleepDetailViewModel(sleepId,dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}