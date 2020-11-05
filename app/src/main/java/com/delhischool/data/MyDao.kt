package com.delhischool.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.delhischool.models.UserDataModel

@Dao
interface MyDao {
    /*
     * to Add the item in database
     * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(resultsBean: UserDataModel?) : Long

    /*
     * to fetch all the saved item from userData table
     * */
    @Query("select * from userData WHERE isTeacher=:isTeacher")
    fun getAllUser(isTeacher: Boolean): LiveData<List<UserDataModel>>

    /*
     * to get the boolean value item present with userId
     * */
    @Query("select * from userData WHERE userId=:userId")
    fun getSingleItem(userId: String?): UserDataModel?


    /*
    * to delete the item
    * */
    @Delete
    suspend fun deleteUser(userDataModel: UserDataModel?)

    /*
     * to update the item if there is some alteration done
     * */
    @Update
    suspend fun updateUser(resultsBean: UserDataModel?)
}