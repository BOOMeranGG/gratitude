package com.orange_infinity.gratitude.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.bolaware.viewstimerstory.Momentz
import com.bolaware.viewstimerstory.MomentzCallback
import com.bolaware.viewstimerstory.MomentzView
import com.orange_infinity.gratitude.R
import kotlinx.android.synthetic.main.activity_citation.*

const val IMAGE_R_ID_KEY = "imageRIdKey"
const val IS_FIRST_KEY = "isFirst"
const val SHOW_SECOND = 9

class CitationActivity : BaseActivity(), MomentzCallback {

    private var isFirst = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citation)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val imageRId = intent?.extras?.getInt(IMAGE_R_ID_KEY)

        val isFirst = intent?.extras?.getBoolean(IS_FIRST_KEY)
        if (isFirst != null && isFirst) {
            this.isFirst = isFirst
        }

        val storyImage = ImageView(this)
        storyImage.setImageResource(imageRId!!)
        storyImage.scaleType = ImageView.ScaleType.CENTER_CROP
        val listOfViews = listOf(MomentzView(storyImage, SHOW_SECOND))
        Momentz(this, listOfViews, layoutRoot, this).start()
    }

    override fun done() {
        finish()
    }

    override fun onNextCalled(view: View, momentz: Momentz, index: Int) {
    }

    override fun onPause() {
        super.onPause()
        if (isFirst) {
            val intent = Intent(this, NoticingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}
