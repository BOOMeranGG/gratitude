package com.orange_infinity.gratitude.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.database.entities.Record
import kotlinx.android.synthetic.main.fiil_record_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class FillRecordFragment : Fragment() {

    private lateinit var activity: Activity

    companion object {
        fun newInstance(activity: Activity): FillRecordFragment {
            val instance = FillRecordFragment()
            instance.activity = activity

            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fiil_record_fragment, container, false)

        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveRecord()
    }

    fun saveRecord() {
        val text = editRecord.text.toString()
        if (text.isNotEmpty()) {
            saveNoticing(text)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun saveNoticing(description: String) {
        object : AsyncTask<Unit, Unit, Unit>() {

            override fun doInBackground(vararg params: Unit) {
                val record = Record()
                val formatForDateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                val currentDate = formatForDateNow.format(Date())
                record.date = currentDate
                record.description = description

                AppDatabase.getInstance(activity).getRecordDao().insert(record)
            }

        }.execute()
    }
}