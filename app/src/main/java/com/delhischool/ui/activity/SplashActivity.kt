package com.delhischool.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.delhischool.R
import com.delhischool.models.UserDataModel
import com.delhischool.ui.viewModel.SplashViewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext = this

        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        splashViewModel.initViewModel()

        splashViewModel.ldNextScreen.observe(this, Observer {
            launchNextScreen(it)
        })
    }

    private fun launchNextScreen(isFirstTime: Boolean) {
        if (isFirstTime) {
            for (x in 1..10) {
                val userDataModel: UserDataModel
                if (x % 2 == 0) {
                    userDataModel = UserDataModel(x.toString(), "Student " + x,
                            "Class " + x, "Section " + x, false, ByteArray(0))
                } else {
                    userDataModel = UserDataModel(x.toString(), "Teacher " + x, "Subject " + x, "",
                            true, ByteArray(0))
                }
                splashViewModel.addUser(userDataModel)
            }
        }

        Intent(mContext, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}