package com.delhischool.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.delhischool.data.MyAppDataBase
import com.delhischool.repository.UserRepository
import com.delhischool.models.UserDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val readAllStudentData: LiveData<List<UserDataModel>>
    val readAllTeacherData: LiveData<List<UserDataModel>>
    private val repository: UserRepository
    val itemAdded = MutableLiveData<Long>()
    var userData = MutableLiveData<UserDataModel>()

    init {
        val userDao = MyAppDataBase.getDatabase(application).myDao()
        repository = UserRepository(userDao)
        readAllStudentData = repository.readAllStudentData
        readAllTeacherData = repository.readAllTeacherData
    }

    fun addUser(user: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            itemAdded.postValue(repository.addUser(user))
        }
    }

    fun updateUser(user: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun deleteUser(user: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }

    fun getSingleItem(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userData.postValue(repository.getSingleItem(userId))
        }
    }
}