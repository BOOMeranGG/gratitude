package com.orange_infinity.gratitude.presenter

import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import com.orange_infinity.gratitude.readBitmapFromDisk

class ImageLoader(private val fileName: String) : AsyncTask<ImageView, Unit, ImageView>() {

    private var bitmap: Bitmap? = null

    override fun doInBackground(vararg params: ImageView?): ImageView {
        bitmap = readBitmapFromDisk(fileName)
        return params[0]!!
    }

    override fun onPostExecute(recordImage: ImageView) {
        if (bitmap != null) {
            val width = bitmap!!.width
            val height = bitmap!!.height
            val divider = (width / 90).coerceAtMost(height / 90)
            recordImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width / divider, height / divider, false))
        }
    }
}