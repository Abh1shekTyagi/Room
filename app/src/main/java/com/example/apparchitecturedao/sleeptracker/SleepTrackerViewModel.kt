/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.apparchitecturedao.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.apparchitecturedao.database.SleepDatabaseDao
import com.example.apparchitecturedao.database.SleepNight
import com.example.apparchitecturedao.formatNights
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    private val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var tonight = MutableLiveData<SleepNight?>()

    val nights = database.getAllNight()

    private val _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar: LiveData<Boolean?> = _showSnackBar

    val myNights = nights.map { nights ->
        formatNights(nights, application.resources)
    }

    private var _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality: LiveData<SleepNight?> = _navigateToSleepQuality

    private var _navigateToSleepDataQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepNight: LiveData<SleepNight?> = _navigateToSleepDataQuality
    fun navigationDone() {
        _navigateToSleepQuality.value = null
        _navigateToSleepQuality.value = null
    }

    val startButtonVisibility = tonight.map {
        it == null
    }

    val stopButtonVisibility = tonight.map {
        it != null
    }

    val clearButtonVisibility = nights.map {
        it.isNotEmpty()
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    fun navigateToSleepDataQuality(sleepNight: SleepNight){
        _navigateToSleepDataQuality.postValue(sleepNight)
    }
    fun onSleepDataQualityNavigated(){
        _navigateToSleepDataQuality.value = null
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        var night = database.getTonight()
        if (night?.endTime != night?.startTime) {
            night = null
        }
        return night
    }

    fun clearAll() {
        viewModelScope.launch {
            clear()
            tonight.postValue(null)
            _showSnackBar.postValue(true)
        }
    }

    fun doneShowingSnackBar() {
        _showSnackBar.value = null
    }

    private suspend fun clear() {
        database.clear()
    }

    fun onStartTracking() {
        viewModelScope.launch {
            insert(SleepNight())
        }
    }


    private suspend fun insert(night: SleepNight) {
        database.insert(night)
        tonight.value = getTonightFromDatabase()
    }

    fun onStopTracking() {
        viewModelScope.launch {
            val tonight = tonight.value ?: return@launch
            tonight.endTime = System.currentTimeMillis()
            update(tonight)
            _navigateToSleepQuality.postValue(tonight)
        }
    }

    private suspend fun update(night: SleepNight) {
        database.update(night)
    }
}

