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
import com.orange_infinity.gratitude.model.preferences.LevelPreferences
import kotlinx.android.synthetic.main.fiil_record_fragment.*
import kotlinx.android.synthetic.main.fiil_record_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*

class FillRecordFragment : Fragment() {

    private lateinit var activity: Activity
    private var countOfRecords: Int = 1
    private var isTop = true

    companion object {
        fun newInstance(activity: Activity, countOfRecords: Int, isTop: Boolean): FillRecordFragment {
            val instance = FillRecordFragment()
            instance.activity = activity
            instance.countOfRecords = countOfRecords
            instance.isTop = isTop

            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fiil_record_fragment, container, false)

        if (countOfRecords < 3) {
            createFirstLevelText(v)
        } else if (countOfRecords < 6) {
            createSecondLevelText(v)
        } else {
            createFreeLevel(v)
        }

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
            checkNewLevel()
        }
    }

    private fun createFirstLevelText(v: View) {
        if (isTop) {
            v.tvTitle.text = "Did anything good happened to you today?"
            v.tvDescription.text = "Met a friend for a lunch? Had a moment of calm?\\n Nothing is too big or too small!"
        } else {
            v.tvTitle.text = "What could you be grateful for today?"
            v.tvDescription.text = "Good weather? Cup of coffee? A loved one?"
        }
    }

    private fun createSecondLevelText(v: View) {
        if (isTop) {
            v.tvTitle.text = "What would you be missing from your life?"
            v.tvDescription.text = "Are there things that you usually don't appreciate? It can be anything from the running water to your partner."
        } else {
            v.tvTitle.text = "What could you be grateful for today?"
            v.tvDescription.text = "Good weather? Cup of coffee? A loved one?"
        }
    }

    private fun createFreeLevel(v: View) {
        if (isTop) {
            v.tvTitle.text = "WHAT ARE YOU GRATEFUL FOR TODAY?"
            v.tvDescription.text = ""
        } else {
            v.tvTitle.text = "WHAT ARE YOU GRATEFUL FOR IN YOUR LIFE?"
            v.tvDescription.text = ""
            v.imgMicrophone.setImageResource(R.drawable.microphone)
            v.imgPaint.setImageResource(R.drawable.paint)
        }
    }

    private fun checkNewLevel() {
        countOfRecords++
        if (countOfRecords % 3 == 0) {
            handleNewLevel(countOfRecords / 3)
        }
    }

    private fun handleNewLevel(level: Int) {
        if (level == 1) { // set up level 2
            LevelPreferences.saveLevel(activity, 2)
        } else if (level == 2) { // set up level 3 (free)
            LevelPreferences.saveLevel(activity, 3)
        } else {    // set up??
            LevelPreferences.saveLevel(activity, 4)
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