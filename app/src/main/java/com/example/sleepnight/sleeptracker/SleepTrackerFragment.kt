package com.example.sleepnight.sleeptracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sleepnight.R
import com.example.sleepnight.database.SleepDatabase
import com.example.sleepnight.databinding.FragmentSleepQualityBinding
import com.example.sleepnight.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

class SleepTrackerFragment : Fragment() {
    private lateinit var binding: FragmentSleepTrackerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSleepTrackerBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application //requireNotNull is a kotlin function that
        // throws a illegal argument exception if the value is null
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application) //reference to STVMF, pass it the data source and application
        val sleepTrackerViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(SleepTrackerViewModel::class.java)
        binding.sleepTrackerViewModel = sleepTrackerViewModel
        binding.setLifecycleOwner(this) // can observe LiveData updates

        //TO DO add GridLayout
        val manager = GridLayoutManager(activity, 3)
        binding.sleepList.layoutManager = manager

        val adapter = SleepNightAdapter()
        binding.sleepList.adapter = adapter


        with(sleepTrackerViewModel){
            navigateToSleepQuality.observe(viewLifecycleOwner) { night ->
                night?.let {
                    findNavController(this@SleepTrackerFragment).navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                    sleepTrackerViewModel.doneNavigating()
                }
            }
            // Add an Observer on the state variable for showing a Snackbar message
            // when the CLEAR button is pressed.
            showSnackbarEvent.observe(viewLifecycleOwner) {
                if (it == true) {
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT // How long to display the message
                    ).show()
                    sleepTrackerViewModel.doneShowingSnackbar()
                }
            }

            nights.observe(viewLifecycleOwner) {
                it?.let {
                    adapter.submitList(it) // the method submitList tell that a new version
                // of the list is available
                }
            }
        }
    }
}