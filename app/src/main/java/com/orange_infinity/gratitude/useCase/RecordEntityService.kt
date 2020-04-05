package com.orange_infinity.gratitude.useCase

import android.app.Activity
import android.content.Intent
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.database.entities.Record
import com.orange_infinity.gratitude.model.preferences.RecordCountPreferences
import com.orange_infinity.gratitude.view.activity.CitationActivity
import com.orange_infinity.gratitude.view.activity.IMAGE_R_ID_KEY

class RecordEntityService(private val activity: Activity) {

    private val recordDao = AppDatabase.getInstance(activity).getRecordDao()

    fun saveRecord(record: Record, isUpdate: Boolean = false) {
        recordDao.insert(record)
        if (!isUpdate) {
            val countOfRecords = getCurrentCountOfRecords() + 1
            RecordCountPreferences.saveRecordCount(activity, countOfRecords)
            showCitationIfNeed(countOfRecords)
        }
    }

    fun getCurrentCountOfRecords(): Int {
        return RecordCountPreferences.getCountOfRecords(activity) ?: 0
    }

    private fun showCitationIfNeed(countOfRecords: Int) {
        when (countOfRecords) {
            1 -> createCitation(R.drawable.citation1)
            2 -> createCitation(R.drawable.citation2)
            3 -> createCitation(R.drawable.citation3)
            4 -> createCitation(R.drawable.citation4)
            5 -> createCitation(R.drawable.citation5)
            6 -> createCitation(R.drawable.citation6)
            7 -> createCitation(R.drawable.citation7)
            8 -> createCitation(R.drawable.citation8)
        }
    }

    private fun createCitation(imageRId: Int) {
        val intent = Intent(activity, CitationActivity::class.java)
        intent.putExtra(IMAGE_R_ID_KEY, imageRId)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        activity.startActivity(intent)
    }
}