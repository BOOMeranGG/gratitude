package com.orange_infinity.gratitude.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.preferences.EntryDateRegister
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lastDate = EntryDateRegister.getLastEntryDate(this)
        Toast.makeText(this, "Last entry date: $lastDate", Toast.LENGTH_LONG).show()
        EntryDateRegister.saveEntryDate(this)

        btnNoticing.setOnClickListener {
            goToNoticing()
        }

        btnJournal.setOnClickListener {
            val intent = Intent(this, JournalActivity::class.java)
            startActivity(intent)
        }

        imgShare.setOnClickListener {
            val intent = Intent(this, ShareActivity::class.java)
            startActivity(intent)
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
                startActivity(intent)
            }
        }.execute()
    }
}
