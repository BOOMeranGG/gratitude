package com.orange_infinity.gratitude.model

import android.content.Context
import android.util.Log
import com.orange_infinity.gratitude.TAG
import java.text.SimpleDateFormat
import java.util.*

private const val FILE_SETTINGS_NAME = "settingsFileName"
private const val LAST_ENTRY_DATE = "lastEntryDate"

object EntryDateRegister {

    fun saveEntryDate(context: Context) {
        val preferences = context.getSharedPreferences(FILE_SETTINGS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()

        val formatForDateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val currentDate = formatForDateNow.format(Date())
        editor.putString(LAST_ENTRY_DATE, currentDate)

        Log.i(TAG, "Save current date: $currentDate")
        editor.apply()
    }

    fun getLastEntryDate(context: Context): String? {
        val preferences = context.getSharedPreferences(FILE_SETTINGS_NAME, Context.MODE_PRIVATE)
        return preferences.getString(LAST_ENTRY_DATE, null)
    }
}