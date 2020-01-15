package com.orange_infinity.gratitude.view.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.database.entities.Record
import kotlinx.android.synthetic.main.activity_noticing.*
import java.text.SimpleDateFormat
import java.util.*

class NoticingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticing)

        getAllNoticing()
        btnCreateNoticing.setOnClickListener {
            saveNoticing()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun saveNoticing() {
        object : AsyncTask<Unit, Unit, Unit>() {

            override fun doInBackground(vararg params: Unit) {
                val record = Record()
                val formatForDateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                val currentDate = formatForDateNow.format(Date())
                record.date = currentDate
                record.description = "testDescription created on: $currentDate"

                AppDatabase.getInstance(applicationContext).getRecordDao().insert(record)
            }

        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    private fun getAllNoticing() {
        object : AsyncTask<Unit, Unit, List<Record>>() {

            override fun doInBackground(vararg params: Unit): List<Record> {
                return AppDatabase.getInstance(applicationContext).getRecordDao().findAll()
            }

            override fun onPostExecute(allRecords: List<Record>) {
                tvNoticing.text = ""
                allRecords.forEach { record ->
                    tvNoticing.text = "${record.id}.) ${record.description} ${tvNoticing.text}\n"
                }
            }
        }.execute()
    }
}
