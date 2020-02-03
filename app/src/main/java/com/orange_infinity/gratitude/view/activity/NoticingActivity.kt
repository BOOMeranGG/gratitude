package com.orange_infinity.gratitude.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.useCase.RecordEntityManager
import com.orange_infinity.gratitude.view.fragment.LevelNoticingFragment
import com.orange_infinity.gratitude.view.fragment.PracticingGratitudeFragment
import com.r0adkll.slidr.Slidr

const val COUNT_OF_RECORDS_KEY = "countOfRecordsKey"

class NoticingActivity : BaseActivity() {

    private var countOfRecords = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticing)
        Slidr.attach(this)

        // Получаем количество сделанных записей через // TODO("Delete, cause it useless")
        countOfRecords = intent?.extras?.getInt(COUNT_OF_RECORDS_KEY) ?: 0
        Log.i(com.orange_infinity.gratitude.TAG, "Count of all records: $countOfRecords")

        goToFillingRecords()
    }

    @SuppressLint("StaticFieldLeak")
    private fun goToFillingRecords() {
        object : AsyncTask<Unit, Unit, Int>() {

            override fun doInBackground(vararg params: Unit?): Int {
                return AppDatabase.getInstance(applicationContext).getRecordDao().getCountOfRecords()
            }

            override fun onPostExecute(countOfRecords: Int) {
                val fm = supportFragmentManager
                var fragment = fm.findFragmentById(R.id.layoutNoticingContainer)

                if (fragment == null) {
                    if (countOfRecords < 6) {
                        //fragment = LevelNoticingFragment.newInstance(this@NoticingActivity)
                        fragment = createLevelNoticing(countOfRecords)
                    } else {
                        fragment = createFreeNoticing(countOfRecords)
                    }
                    fm.beginTransaction()
                        .add(R.id.layoutNoticingContainer, fragment)
                        .commit()
                }

            }

            fun createLevelNoticing(countOfRecords: Int): LevelNoticingFragment {
                return LevelNoticingFragment.newInstance(this@NoticingActivity, countOfRecords)
            }

            fun createFreeNoticing(countOfRecords: Int): PracticingGratitudeFragment {
                return PracticingGratitudeFragment.newInstance(this@NoticingActivity, countOfRecords)
            }
        }.execute()
    }
}
