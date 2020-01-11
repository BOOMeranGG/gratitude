package com.orange_infinity.gratitude

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

const val TAG = "MAIN_TAG"

fun showFcmToken(context: Context) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.i(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result?.token
                Log.i(TAG, token)
                Toast.makeText(context, token, Toast.LENGTH_SHORT).show()
            })
}