package com.example.sleepnight.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.internal.synchronized
import java.time.Instant

@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
// we have only one table so we supply with SleepNight class, if you have more than 1 table add them all
//version starts w 1, cause that is the great first version, whenever you change the schema you
// will have to up the version number or your app will not work anymore
abstract class SleepDatabase : RoomDatabase() {
    abstract val sleepDatabaseDao: SleepDatabaseDao
    companion object {
        @Volatile // help to make sure the value of INSTANCE is always up to date and the same
        // to all execution threats
        private var INSTANCE: SleepDatabase? = null
        // instance will keep a reference to the database once we have one
        fun getInstance(context: Context) :  SleepDatabase {
            // one thread of execution at a time can enter this block of code
            kotlin.synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    )
                    .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}