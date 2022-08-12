package com.example.sleepnight.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao {
    @Insert //when we call insert, room creates the row from the entity object and inserts
    // it into the database
    fun insert(night: SleepNight)

    // then we want to be able to update it, for example with a new end-time or a sleep quality
    @Update
    fun update(night: SleepNight)

    // select all columns from table, and return the rows where the nightID matches the supplied key
    @Query(value = "SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    // get one specific night based on this key
    fun get(key: Long): SleepNight // return the corresponding table row as an instance of Sleepnight

    /*@Delete
    fun deleteAllNights(nights: List<SleepNight>): Int // great to deleting specifics entries but not efficient for clearing
}*/
    @Query(value = "DELETE FROM daily_sleep_quality_table") //every row is deleted
    fun clear()
    @Query(value = "SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>> // getting back a list of SleepNight which is expected
    // however it's actually live data

    @Query(value = "SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getTonight(): SleepNight? // returns the most recent night by looking at AllNights
    // ordering and returning only one indicated by limit one
    // it is the one with the highest ID, which is the latest night
    // it is nullable because in the beginning and after we clear, there is no tonight
}
