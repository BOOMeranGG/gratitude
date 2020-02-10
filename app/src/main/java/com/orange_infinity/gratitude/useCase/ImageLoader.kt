package com.orange_infinity.gratitude.useCase

import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import com.orange_infinity.gratitude.model.RecordImagesCache
import com.orange_infinity.gratitude.readBitmapFromDisk
import com.orange_infinity.gratitude.view.activity.interfaces.ImageLoaderOwner

const val IMAGE_MINI = "_mini"

class ImageLoader(
    private val fileName: String,
    private val loaderOwner: ImageLoaderOwner
) : AsyncTask<ImageView?, Unit, ImageView?>() {

    var widthCompress = 90
    var heightCompress = 90
    private var bitmap: Bitmap? = null

    override fun doInBackground(vararg params: ImageView?): ImageView? {
        bitmap = RecordImagesCache.imageCache.get(fileName)
        if (bitmap == null) {
            bitmap = readBitmapFromDisk(fileName)
        }
        return params[0]
    }

    override fun onPostExecute(recordImage: ImageView?) {
        if (bitmap != null) {
            val width = bitmap!!.width
            val height = bitmap!!.height
            val divider = (width / widthCompress).coerceAtMost(height / heightCompress)


            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width / divider, height / divider, false)
            if (fileName.contains(IMAGE_MINI)) {
                RecordImagesCache.imageCache.put(fileName, scaledBitmap)
            }

            recordImage?.setImageBitmap(scaledBitmap)
        }
        loaderOwner.onLoadComplete()
    }
}