package com.orange_infinity.gratitude.view.activity

import android.os.Bundle
import android.widget.Toast
import com.orange_infinity.gratitude.R
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        btnShare.setOnClickListener {
            Toast.makeText(this, "Coming soon!", Toast.LENGTH_LONG).show()
        }
    }
}
