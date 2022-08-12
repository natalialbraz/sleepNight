package com.example.sleepnight.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sleepnight.database.SleepDatabaseDao

class SleepTrackerViewModel(
    // VM needs access to the data in database, which is through the interface defined in the DAO
    val database: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) // is the same asd VM, but it takes the application context as a
// parameter and makes it available as a property
{
}