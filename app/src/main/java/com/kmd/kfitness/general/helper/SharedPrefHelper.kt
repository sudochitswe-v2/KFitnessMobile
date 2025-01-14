package com.kmd.kfitness.general.helper

import android.content.Context
import android.content.SharedPreferences
import com.kmd.kfitness.general.constant.ConstantKey

class SharedPrefHelper(context: Context) {
    private val sharedPreferences:SharedPreferences =
        context.getSharedPreferences(ConstantKey.sharedPref,0)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
    fun removeData(key: String){
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
    fun clearData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}