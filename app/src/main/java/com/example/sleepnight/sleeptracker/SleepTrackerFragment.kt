package com.example.sleepnight.sleeptracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.sleepnight.R
import com.example.sleepnight.database.SleepDatabase
import com.example.sleepnight.databinding.FragmentSleepQualityBinding
import com.example.sleepnight.databinding.FragmentSleepTrackerBinding

class SleepTrackerFragment : Fragment() {
    private lateinit var binding: FragmentSleepTrackerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSleepTrackerBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    val application = requireNotNull(this.activity).application
    val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
    val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
    val sleepTrackerViewModel =
        ViewModelProvider.o


}

