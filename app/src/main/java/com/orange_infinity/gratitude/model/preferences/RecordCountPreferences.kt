package com.orange_infinity.gratitude.model.preferences

import android.content.Context
import android.util.Log
import com.orange_infinity.gratitude.TAG

private const val FILE_SETTINGS_NAME = "settingsFileName"
private const val COUNT_OF_RECORDS = "levelKey"

object RecordCountPreferences {

    fun saveRecordCount(context: Context, countOfRecords: Int) {
        val preferences = context.getSharedPreferences(FILE_SETTINGS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putInt(COUNT_OF_RECORDS, countOfRecords)

        Log.i(TAG, "Save current countOfRecords: $countOfRecords")
        editor.apply()
    }

    fun getCountOfRecords(context: Context): Int? {
        val preferences = context.getSharedPreferences(FILE_SETTINGS_NAME, Context.MODE_PRIVATE)
        return preferences.getInt(COUNT_OF_RECORDS, 0)
    }
}