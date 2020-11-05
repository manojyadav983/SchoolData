package com.delhischool.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.delhischool.models.UserDataModel

@Database(entities = [UserDataModel::class], version = 1, exportSchema = false)
abstract class MyAppDataBase : RoomDatabase() {
    abstract fun myDao(): MyDao

    companion object {
        @Volatile
        private var INSTANCE: MyAppDataBase? = null

        fun getDatabase(context: Context): MyAppDataBase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyAppDataBase::class.java,
                        "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}