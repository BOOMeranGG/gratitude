package com.orange_infinity.gratitude.model.preferences

import android.content.Context
import android.util.Log
import com.orange_infinity.gratitude.TAG

private const val FILE_SETTINGS_NAME = "settingsFileName"
private const val LEVEL_KEY = "levelKey"

object LevelPreferences {

    fun saveLevel(context: Context, level: Int) {
        val preferences = context.getSharedPreferences(FILE_SETTINGS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putInt(LEVEL_KEY, level)

        Log.i(TAG, "Save current level: $level")
        editor.apply()
    }

    fun getLevel(context: Context): Int? {
        val preferences = context.getSharedPreferences(FILE_SETTINGS_NAME, Context.MODE_PRIVATE)
        return preferences.getInt(LEVEL_KEY, 0)
    }
}