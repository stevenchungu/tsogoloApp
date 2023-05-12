package com.example.tsogolo.util

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppPreferences private constructor(context: Context) {

    private val preferences = context.getSharedPreferences("com.tsogolo", Context.MODE_PRIVATE)

    private val _isDarkTheme: MutableStateFlow<Boolean> =
        MutableStateFlow(preferences.getBoolean(PREF_THEME, false))
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun setIsDarkTheme(isDark: Boolean) {
        preferences.edit().putBoolean(PREF_THEME, isDark).apply()
        _isDarkTheme.compareAndSet(!isDark, isDark)
    }

    companion object {
        private const val PREF_THEME = "is_dark_theme"
        private var instance: AppPreferences? = null
        fun getInstance(context: Context): AppPreferences {
            if (instance == null) {
                instance = AppPreferences(context)
            }
            return instance!!
        }
    }
}