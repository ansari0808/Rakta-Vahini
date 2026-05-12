package com.raktavahini.ui.theme

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    
    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    init {
        _isDarkMode.value = prefs.getBoolean("dark_mode", true)
    }

    fun toggleTheme() {
        val current = _isDarkMode.value ?: true
        val newValue = !current
        _isDarkMode.value = newValue
        
        prefs.edit().putBoolean("dark_mode", newValue).apply()
        
        AppCompatDelegate.setDefaultNightMode(
            if (newValue) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
