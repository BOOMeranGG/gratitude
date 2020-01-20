package com.orange_infinity.gratitude.view.activity

import android.content.Intent
import android.os.Bundle
import com.orange_infinity.gratitude.R
import kotlinx.android.synthetic.main.activity_citation.*

const val IMAGE_R_ID_KEY = "imageRIdKey"
const val IS_FIRST_KEY = "isFirst"

class CitationActivity : BaseActivity() {

    private var isFirst = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citation)

        val imageRId = intent?.extras?.getInt(IMAGE_R_ID_KEY)
        if (imageRId != null) {
            imgMain.setImageResource(imageRId)
        }

        imgMain.setOnClickListener {
            finish()
        }

        val isFirst = intent?.extras?.getBoolean(IS_FIRST_KEY)
        if (isFirst != null && isFirst) {
            this.isFirst = isFirst
        }
    }

    override fun onPause() {
        super.onPause()
        if (isFirst) {
            val intent = Intent(this, NoticingActivity::class.java)
            intent.putExtra(COUNT_OF_RECORDS_KEY, 0)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}
