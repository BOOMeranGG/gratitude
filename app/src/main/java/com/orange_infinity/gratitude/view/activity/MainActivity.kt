package com.orange_infinity.gratitude.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.orange_infinity.gratitude.TAG
import com.orange_infinity.gratitude.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This is code to get FCM token
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.i(TAG, "getInstanceId failed", task.exception)
//                    return@OnCompleteListener
//                }
//
//                // Get new Instance ID token
//                val token = task.result?.token
//
//                // Log and toast
//                //val msg = getString(R.string.msg_token_fmt, token)
//                Log.d(TAG, token)
//                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
//            })
    }
}
