package com.delhischool.repository

import androidx.lifecycle.LiveData
import com.delhischool.data.MyDao
import com.delhischool.models.UserDataModel

class UserRepository(private val userDao: MyDao) {

    val readAllStudentData: LiveData<List<UserDataModel>> = userDao.getAllUser(false)
    val readAllTeacherData: LiveData<List<UserDataModel>> = userDao.getAllUser(true)

    suspend fun addUser(user: UserDataModel): Long {
        return userDao.addUser(user)
    }

    suspend fun updateUser(user: UserDataModel) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: UserDataModel) {
        userDao.deleteUser(user)
    }

    fun getSingleItem(userId: String) : UserDataModel{
       return userDao.getSingleItem(userId)!!
    }
}