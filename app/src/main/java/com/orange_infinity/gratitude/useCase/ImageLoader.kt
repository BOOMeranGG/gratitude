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
            val divider = (width / 90).coerceAtMost(height / 90)

            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width / divider, height / divider, false)
            RecordImagesCache.imageCache.put(fileName, scaledBitmap)

            recordImage?.setImageBitmap(scaledBitmap)
        }
        loaderOwner.onLoadComplete()
    }
}