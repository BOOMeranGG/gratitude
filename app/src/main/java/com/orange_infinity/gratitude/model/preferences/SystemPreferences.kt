package com.orange_infinity.gratitude.model.preferences

import android.content.Context
import android.util.Log
import com.orange_infinity.gratitude.TAG

private const val FILE_SETTINGS_NAME = "settingsFileName"
const val IS_JOURNAL_NOT_EMPTY = "isJournalEmpty"

object SystemPreferences {

    fun saveBoolean(context: Context, settingName: String, isCondition: Boolean) {
        val preferences = context.getSharedPreferences(FILE_SETTINGS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putBoolean(settingName, isCondition)

        Log.i(TAG, "Save boolean condition: $isCondition")
        editor.apply()
    }

    fun getBoolean(context: Context, settings: String): Boolean? {
        val preferences = context.getSharedPreferences(FILE_SETTINGS_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(settings, false)
    }
}