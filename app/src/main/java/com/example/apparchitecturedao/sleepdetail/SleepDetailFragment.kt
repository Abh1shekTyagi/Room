package com.example.apparchitecturedao.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apparchitecturedao.database.SleepDatabase
import com.example.apparchitecturedao.databinding.FragmentSleepDetailBinding

class SleepDetailFragment : Fragment() {

    private lateinit var binding: FragmentSleepDetailBinding
    private lateinit var viewModel: SleepDetailViewModel
    private lateinit var viewModelFactory: SleepDetailViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSleepDetailBinding.inflate(inflater,container,false)
        val application = requireNotNull(this.activity).application
        val database = SleepDatabase.getInstance(application).sleepDatabaseDao
        val args = SleepDetailFragmentArgs.fromBundle(requireArguments()).sleepNightKey
        viewModelFactory = SleepDetailViewModelFactory(args,database)
        viewModel = ViewModelProvider(this,viewModelFactory)[SleepDetailViewModel::class.java]
        binding.sleepDetailViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        viewModel.navigateBack.observe(viewLifecycleOwner){
            it?.let {
                findNavController().popBackStack()
            }
        }
    }
}