package com.example.sleepnight.sleeptracker

import android.app.Application
import android.content.res.Resources
import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import android.text.method.TextKeyListener.clear
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sleepnight.database.SleepDatabaseDao
import com.example.sleepnight.database.SleepNight
import com.example.sleepnight.formatNights
import kotlinx.coroutines.*

class SleepTrackerViewModel(
    // VM needs access to the data in database, which is through the interface defined in the DAO
    val database: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) // is the same as VM, but it takes the application context as a
// parameter and makes it available as a property
{
    private var viewModelJob = Job() // allows us to cancel all coroutines started by VM when the VM is no longer used and destroyed

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob) // to get the scope we ask for an instance of
    // coroutine scope, and we pass in a dispatcher and the job
    // Dispatchers means coroutines launched in the UI scope will run on the main thread
    private var tonight = MutableLiveData<SleepNight?>() // we want to be able to observe it
    val nights = database.getAllNights() //just show the object reference, to see the contents we transform this data

    // executed everytime night receives new data from the database
    // Converted nights to Spanned for displaying
    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources) // will gave us access to our string resources
    }

    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }
    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }
    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    // Variable that tells the Fragment to navigate to a specific [SleepQualityFragment]
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality // the fragment can observe

    fun doneNavigating(){
        //_navigateToSleepQuality.value = null // after navigating we need to set the navigation variable
    }

    init {
        initializeTonight()
    }
    private fun initializeTonight(){
        uiScope.launch {
            // we are using a coroutine tonight from the database, so we aren't blocking the UI while
            // waiting for the result
            tonight.value  = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }
    // executes when the START button is clicked
    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }
    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO){
            database.insert(night)
        }
    }
    // executes when the STOP button is clicked
    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch // specifying which function among several
            // nested ones this statement returns from
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }
    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }
    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackbarEvent.value = true
        }
    }
    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }
}