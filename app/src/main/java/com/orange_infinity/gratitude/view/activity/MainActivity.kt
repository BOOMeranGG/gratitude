package com.orange_infinity.gratitude.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.preferences.EntryDateRegister
import com.orange_infinity.gratitude.model.preferences.RecordCountPreferences
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_READ_STORAGE = 113

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EntryDateRegister.saveEntryDate(this)
        btnReflecting.isEnabled = false
        btnPractice.isEnabled = false

        requestPermission(this)

        btnNoticing.setOnClickListener {
            if (RecordCountPreferences.getCountOfRecords(this) == 0) {
                val intent = Intent(this, CitationActivity::class.java)
                intent.putExtra(IMAGE_R_ID_KEY, R.drawable.citate1_new)
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

        layoutShare.setOnClickListener {
            val intent = Intent(this, ShareActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        //showFcmToken(this)
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
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setEnabling() {
        setEnableToAll(true)
        val countOfRecords = RecordCountPreferences.getCountOfRecords(this) ?: 0
        when {
            countOfRecords == 0 -> {
                btnReflecting.isEnabled = false
                btnPractice.isEnabled = false
                btnJournal.isEnabled = false
            }
            countOfRecords < 3 -> {
                btnReflecting.isEnabled = false
                btnPractice.isEnabled = false
            }
            countOfRecords < 6 -> {
                btnNoticing.isEnabled = false
                btnPractice.isEnabled = false
            }
            else -> {
                btnNoticing.isEnabled = false
                btnReflecting.isEnabled = false
            }
        }
    }

    private fun setEnableToAll(isEnable: Boolean) {
        btnNoticing.isEnabled = isEnable
        btnReflecting.isEnabled = isEnable
        btnPractice.isEnabled = isEnable
        btnJournal.isEnabled = isEnable
    }

    @SuppressLint("StaticFieldLeak")
    private fun goToNoticing() {
        object : AsyncTask<Unit, Unit, Int>() {

            override fun doInBackground(vararg params: Unit?): Int {
                return AppDatabase.getInstance(applicationContext).getRecordDao().getCountOfRecords()
            }

            override fun onPostExecute(result: Int) {
                val intent = Intent(this@MainActivity, NoticingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }.execute()
    }
}
