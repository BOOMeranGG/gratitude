package com.orange_infinity.gratitude.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.database.entities.Record
import com.orange_infinity.gratitude.useCase.IMAGE_MINI
import com.orange_infinity.gratitude.useCase.ImageLoader
import com.orange_infinity.gratitude.view.activity.interfaces.ImageLoaderOwner

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadApp()
    }

    @SuppressLint("StaticFieldLeak")
    private fun loadApp() {
        object : AsyncTask<Unit, Unit, Unit>(), ImageLoaderOwner {

            private val countOfMaxLoadRecords = 15
            private var countOfLoadRecords = 0
            private var currentCount = 0

            override fun doInBackground(vararg params: Unit?) {
                val records = AppDatabase.getInstance(applicationContext).getRecordDao().findAllWithLimit()
                countOfLoadRecords = if (records.size > countOfMaxLoadRecords) {
                    countOfMaxLoadRecords
                } else {
                    records.size
                }
                if (countOfLoadRecords == 0) {
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    records.parallelStream().forEach { record ->
                        loadImages(record)
                    }
                } else {
                    records.forEach { record ->
                        loadImages(record)
                    }
                }
            }

            fun loadImages(record: Record) {
                ImageLoader(record.imageName!! + IMAGE_MINI, this).execute(null)
            }

            override fun onLoadComplete() {
                currentCount++
                if (currentCount == countOfLoadRecords) {
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.execute()
    }
}
