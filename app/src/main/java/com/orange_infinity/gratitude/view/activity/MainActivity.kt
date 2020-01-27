package com.orange_infinity.gratitude.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.preferences.EntryDateRegister
import com.orange_infinity.gratitude.model.preferences.IS_JOURNAL_NOT_EMPTY
import com.orange_infinity.gratitude.model.preferences.LevelPreferences
import com.orange_infinity.gratitude.model.preferences.SystemPreferences
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_READ_STORAGE = 113

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lastDate = EntryDateRegister.getLastEntryDate(this)
        EntryDateRegister.saveEntryDate(this)

        btnReflecting.isEnabled = false
        btnPractice.isEnabled = false

        requestPermission(this)

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
        setEnabling()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_STORAGE) {
            if (hasPermissions(this)) {
                //handleNetwork()
            } else {
                requestPermission(this)
            }
        }
    }

    private fun requestPermission(context: Activity) {
        if (!hasPermissions(context)) {
            ActivityCompat.requestPermissions(
                context,
                //arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_READ_STORAGE
            )
        } else {
            //handleNetwork()
        }
    }

    private fun hasPermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            //Manifest.permission.READ_EXTERNAL_STORAGE
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setEnabling() {
//        setEnableFalseToAll()
//        val level = LevelPreferences.getLevel(this)
//        when (level) {
//            null, 0, 1 -> {
//                btnNoticing.isEnabled = true
//            }
//            2 -> {
//                btnReflecting.isEnabled = true
//            }
//            else -> {
//                btnPractice.isEnabled = true
//            }
//        }
//        val isJournalNotEmpty = SystemPreferences.getBoolean(this, IS_JOURNAL_NOT_EMPTY)
//        btnJournal.isEnabled = isJournalNotEmpty ?: false
    }

    private fun setEnableFalseToAll() {
        btnNoticing.isEnabled = false
        btnReflecting.isEnabled = false
        btnPractice.isEnabled = false
        btnJournal.isEnabled = false
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
