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

package com.example.apparchitecturedao.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apparchitecturedao.database.SleepDatabaseDao
import kotlinx.coroutines.launch

class SleepQualityViewModel(
    private val sleepNightId: Long = 0L,
    val database: SleepDatabaseDao
): ViewModel(){

    private var _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?> = _navigateToSleepTracker

    fun onSetSleepQuality(quality: Int){
        viewModelScope.launch {
            update(sleepNightId, quality)
        }
    }

    private suspend fun update(id: Long, sleepQuality: Int){
        val sleepNight = database.get(id) ?: return
        sleepNight.sleepRating = sleepQuality
        database.update(sleepNight)
        _navigateToSleepTracker.postValue(true)
    }

    fun onNavigateComplete() {
        _navigateToSleepTracker.value = null
    }
}
