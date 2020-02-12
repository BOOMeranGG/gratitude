package com.orange_infinity.gratitude.view.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.FacebookContent
import kotlinx.android.synthetic.main.activity_share.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class ShareActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        btnShare.shareContent = FacebookContent.CONTENT
    }

    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                "com.orange_infinity.gratitude",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d(com.orange_infinity.gratitude.TAG, Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }
}
