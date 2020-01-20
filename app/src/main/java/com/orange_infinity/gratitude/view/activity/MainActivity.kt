package com.orange_infinity.gratitude.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.preferences.EntryDateRegister
import com.orange_infinity.gratitude.model.preferences.LevelPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lastDate = EntryDateRegister.getLastEntryDate(this)
        Toast.makeText(this, "Last entry date: $lastDate", Toast.LENGTH_LONG).show()
        EntryDateRegister.saveEntryDate(this)

        btnReflecting.isEnabled = false
        btnPractice.isEnabled = false

        btnNoticing.setOnClickListener {
            val level = LevelPreferences.getLevel(this)
            if (level != null && level == 0) {
                LevelPreferences.saveLevel(this, 1)
                val intent = Intent(this, CitationActivity::class.java)
                intent.putExtra(IMAGE_R_ID_KEY, R.drawable.level1)
                intent.putExtra(IS_FIRST_KEY, true)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            } else {
                goToNoticing()
            }
        }

        btnReflecting.setOnClickListener {
            goToNoticing()
        }

        btnPractice.setOnClickListener {
            goToNoticing()
        }

        btnJournal.setOnClickListener {
            val intent = Intent(this, JournalActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        imgShare.setOnClickListener {
            val intent = Intent(this, ShareActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        checkLevel()
    }

    private fun checkLevel() {
        val level = LevelPreferences.getLevel(this)
        if (level == null || level == 0) {
            //LevelPreferences.saveLevel(this, 1)
        } else if (level == 2) {
            btnReflecting.isEnabled = true
        } else if (level == 3) {
            btnReflecting.isEnabled = true
            btnPractice.isEnabled = true
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun goToNoticing() {
        object : AsyncTask<Unit, Unit, Int>() {

            override fun doInBackground(vararg params: Unit?): Int {
                return AppDatabase.getInstance(applicationContext).getRecordDao().getCountOfRecords()
            }

            override fun onPostExecute(result: Int) {
                val intent = Intent(this@MainActivity, NoticingActivity::class.java)
                intent.putExtra(COUNT_OF_RECORDS_KEY, result)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }.execute()
    }
}
