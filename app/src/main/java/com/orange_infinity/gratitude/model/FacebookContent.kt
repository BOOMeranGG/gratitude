package com.orange_infinity.gratitude.model

import android.net.Uri
import com.facebook.share.model.ShareLinkContent

class FacebookContent {

    companion object {
        val CONTENT: ShareLinkContent = ShareLinkContent.Builder()
            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.orange_infinity.gratitude"))
            .setQuote("Hey, this Gratitude app has helped me to notice the positive things in my life," +
                    " appreciate it more, and be more happy with this simple 5 minute practice." +
                    " It is free and you can get it at"
            )
            .build()
    }
}