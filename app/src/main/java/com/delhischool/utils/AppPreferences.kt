package com.delhischool.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.delhischool.R

class AppPreferences(mContext: Context) {
    private val pref: SharedPreferences = mContext.getSharedPreferences(
            mContext.getString(R.string.app_name),
            Activity.MODE_PRIVATE
    ) as SharedPreferences
    
    fun getBoolean(key: String, defaultValue: Boolean): Boolean? {
        return pref.getBoolean(key, defaultValue)
    }
}