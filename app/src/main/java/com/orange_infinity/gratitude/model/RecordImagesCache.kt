package com.orange_infinity.gratitude.model

import android.graphics.Bitmap
import android.util.LruCache


//private const val CACHE_SIZE_OF_BYTES = 13107200 / 2 //12.5 / 2 MB ~47 / 2 images(in my case)
private const val CACHE_SIZE_OF_BITMAPS = 20

object RecordImagesCache {

    val imageCache: LruCache<String, Bitmap?> = object : LruCache<String, Bitmap?>(CACHE_SIZE_OF_BITMAPS) {
        override fun sizeOf(key: String, bitmap: Bitmap?): Int {
            //return bitmap?.byteCount ?: 0
            return 1
        }
    }
}