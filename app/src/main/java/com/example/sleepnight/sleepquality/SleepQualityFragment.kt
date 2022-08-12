package com.example.sleepnight.sleepquality

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sleepnight.R
import com.example.sleepnight.databinding.FragmentSleepQualityBinding

class SleepQualityFragment : Fragment() {

    private val viewModel: SleepQualityViewModel by viewModels()
    private lateinit var binding: FragmentSleepQualityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSleepQualityBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}