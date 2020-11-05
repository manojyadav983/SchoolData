package com.delhischool.ui.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.delhischool.data.MyAppDataBase
import com.delhischool.models.UserDataModel
import com.delhischool.repository.UserRepository
import com.delhischool.utils.AppPreferences
import com.delhischool.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val appContext: Context = application.applicationContext
    val ldNextScreen = MutableLiveData<Boolean>()
    private val mPref = AppPreferences(appContext)
    private val repository: UserRepository

    init {
        val userDao = MyAppDataBase.getDatabase(application).myDao()
        repository = UserRepository(userDao)
    }

    fun initViewModel(){
        launchActivity()
    }

    fun addUser(user: UserDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    private fun launchActivity() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            val isFirstTime = mPref.getBoolean(Constant.isFirstTime, true)
            ldNextScreen.postValue(isFirstTime)

            if (!isFirstTime!!) {
                mPref.getBoolean(Constant.isFirstTime, false)
            }
        }
    }
}