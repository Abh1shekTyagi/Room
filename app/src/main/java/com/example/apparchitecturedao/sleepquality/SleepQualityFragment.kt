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

package com.example.android.trackmysleepquality.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.apparchitecturedao.database.SleepDatabase
import com.example.apparchitecturedao.databinding.FragmentSleepQualityBinding
import com.example.apparchitecturedao.sleepquality.SleepQualityViewModel
import com.example.apparchitecturedao.sleepquality.SleepQualityViewModelFactory

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment() {

    private lateinit var binding: FragmentSleepQualityBinding
    private lateinit var viewModel: SleepQualityViewModel
    private lateinit var viewModelFactory: SleepQualityViewModelFactory
    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Get a reference to the binding object and inflate the fragment views.
        binding = FragmentSleepQualityBinding.inflate(inflater, container, false)
        val nightKey = SleepQualityFragmentArgs.fromBundle(requireArguments()).sleepNightKey
        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        viewModelFactory = SleepQualityViewModelFactory(nightKey,dataSource)
        viewModel = ViewModelProvider(this,viewModelFactory).get()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        viewModel.navigateToSleepTracker.observe(viewLifecycleOwner){
            it?.let {
                findNavController().navigate(SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                viewModel.onNavigateComplete()
            }
        }
    }
}
