package com.orange_infinity.gratitude.view.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.view.fragment.LevelNoticingFragment
import com.orange_infinity.gratitude.view.fragment.PracticingGratitudeFragment
import com.r0adkll.slidr.Slidr

class NoticingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticing)
        Slidr.attach(this)

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
                    fragment = if (countOfRecords < 6) {
                        createLevelNoticing(countOfRecords)
                    } else {
                        createFreeNoticing(countOfRecords)
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
