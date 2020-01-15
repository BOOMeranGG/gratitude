package com.orange_infinity.gratitude.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.EntryDateRegister
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lastDate = EntryDateRegister.getLastEntryDate(this)
        Toast.makeText(this, "Last entry date: $lastDate", Toast.LENGTH_LONG).show()
        EntryDateRegister.saveEntryDate(this)

        btnNoticing.setOnClickListener {
            val intent = Intent(this, NoticingActivity::class.java)
            startActivity(intent)
        }
    }
}
