package com.orange_infinity.gratitude.view.activity

import android.os.Bundle
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.useCase.IMAGE_MINI
import com.orange_infinity.gratitude.useCase.ImageLoader
import com.orange_infinity.gratitude.view.activity.interfaces.ImageLoaderOwner
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_image_viewer.*

const val IMAGE_NAME_KEY = "imageNameKey"

class ImageViewerActivity : BaseActivity(), ImageLoaderOwner {

    private lateinit var imageName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)
        Slidr.attach(this)

        imageName = intent?.extras?.getString(IMAGE_NAME_KEY) ?: ""
        ImageLoader(imageName + IMAGE_MINI, this).execute(imgMain)

        imgMain.setOnClickListener {
            finish()
        }
    }

    override fun onLoadComplete() {
        ImageLoader(imageName, this).execute(imgMain)
    }
}
