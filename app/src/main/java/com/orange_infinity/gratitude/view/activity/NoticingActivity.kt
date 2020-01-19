package com.orange_infinity.gratitude.view.activity

import android.os.Bundle
import android.util.Log
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.view.fragment.LevelNoticingFragment

const val COUNT_OF_RECORDS_KEY = "countOfRecordsKey"

class NoticingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticing)

        // Получаем количество сделанных записей через
        val countOfRecords = intent?.extras?.getInt(COUNT_OF_RECORDS_KEY)
        Log.i(com.orange_infinity.gratitude.TAG, "Count of all records: $countOfRecords")

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.layoutNoticingContainer)

        if (fragment == null) {
            fragment = LevelNoticingFragment.newInstance(this)
            fm.beginTransaction()
                .add(R.id.layoutNoticingContainer, fragment)
                .commit()
        }
    }
}
