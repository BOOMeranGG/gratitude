package com.orange_infinity.gratitude.view.activity

import android.graphics.Point
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.useCase.IMAGE_MINI
import com.orange_infinity.gratitude.useCase.ImageLoader
import com.orange_infinity.gratitude.view.activity.interfaces.ImageLoaderOwner
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_image_viewer.*


const val IMAGE_NAME_KEY = "imageNameKey"

class ImageViewerActivity : BaseActivity(), ImageLoaderOwner {

    private lateinit var imageName: String
    private var isLoadFullImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)
        Slidr.attach(this)

        imageName = intent?.extras?.getString(IMAGE_NAME_KEY + IMAGE_MINI) ?: ""
        ImageLoader(imageName, this).execute(imgMain)

        imgMain.setOnClickListener {
            finish()
        }
    }

    override fun onLoadComplete() {
        if (!isLoadFullImage) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val height = size.y
            //val imageLoader = ImageLoader(imageName, this)
            val layoutMain = findViewById<ConstraintLayout>(R.id.layoutMain)
            ImageLoader(imageName, this, 1200, 1200).execute(imgMain)

            isLoadFullImage = true
        }
    }
}
