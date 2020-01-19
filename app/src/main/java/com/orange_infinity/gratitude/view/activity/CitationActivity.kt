package com.orange_infinity.gratitude.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orange_infinity.gratitude.R

const val IMAGE_R_ID_KEY = "imageRIdKey"

class CitationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citation)

        //val countOfRecords = intent?.extras?.getInt(IMAGE_R_ID_KEY)
    }
}
