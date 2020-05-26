package com.orange_infinity.gratitude.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.util.StringUtil
import com.google.gson.Gson
import com.google.zxing.common.StringUtils
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.convertDpToPixel
import com.orange_infinity.gratitude.model.database.entities.Record
import com.orange_infinity.gratitude.useCase.AudioCompleteListener
import com.orange_infinity.gratitude.useCase.AudioController
import com.orange_infinity.gratitude.useCase.ImageLoader
import com.orange_infinity.gratitude.useCase.RecordEntityManager
import com.orange_infinity.gratitude.view.activity.interfaces.ImageLoaderOwner
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_solo_record.*

const val RECORD_JSON_VALUE_KEY = "recordJsonValueKey"
const val RESULT_JSON_RECORD = "resultJsonRecord"
const val SOLO_RECORD_RESULT_CODE = 0

class SoloRecordActivity : BaseActivity(), ImageLoaderOwner, AudioCompleteListener {

    private lateinit var record: Record
    private val audioController = AudioController(this)
    private var firstSoundRunning = false
    private var secondSoundRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_record)
        Slidr.attach(this)

        val recordJson = intent.getStringExtra(RECORD_JSON_VALUE_KEY)
        Log.d(com.orange_infinity.gratitude.TAG, recordJson)
        record = Gson().fromJson(recordJson, Record::class.java)

        initViews()

        imgBackArrow.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            record.description = editDescription.text.toString()
            record.descriptionSecond = editDescriptionSecond.text.toString()

            RecordEntityManager().save(this, record, true)
            val recordJson2 = Gson().toJson(record)
            val intent = Intent()
            intent.putExtra(RESULT_JSON_RECORD, recordJson2)
            setResult(SOLO_RECORD_RESULT_CODE, intent)
            finish()
        }
    }

    override fun onAudioComplete() {
        imgSoundPlay.setImageResource(R.drawable.ic_play)
        imgSoundPlaySecond.setImageResource(R.drawable.ic_play)
        lineFirst.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMusicLine))
        lineSecond.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMusicLine))

        firstSoundRunning = false
        secondSoundRunning = false
    }

    private fun initViews() {
        tvDate.text = record.date
        editDescription.setText(record.description)
        editDescriptionSecond.setText(record.descriptionSecond)

        setUpImages()
        setUpSounds()
        setUpEditText()
    }

    private fun setUpEditText() {
        if (record.description.isEmpty()) {
            layoutContentFirst.visibility = View.GONE
        }
        if (record.descriptionSecond.isEmpty()) {
            layoutContentSecond.visibility = View.GONE
        }
    }

    private fun setUpSounds() {
        if (!record.soundName.isNullOrBlank() && audioController.isAudioExist(record.soundName!!)) {
            layoutSoundFirst.visibility = View.VISIBLE

            imgSoundPlay.setOnClickListener {
                if (!firstSoundRunning) {
                    audioController.startPlay(record.soundName!!)
                    imgSoundPlay.setImageResource(R.drawable.ic_stop)
                    firstSoundRunning = true
                    lineFirst.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDarkRed))
                    Log.d(com.orange_infinity.gratitude.TAG, "First music start playing")
                } else {
                    audioController.stopPlay()
                    imgSoundPlay.setImageResource(R.drawable.ic_play)
                    firstSoundRunning = false
                    lineFirst.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMusicLine))
                    Log.d(com.orange_infinity.gratitude.TAG, "First music stop playing")
                }
            }
        } else {
            layoutSoundFirst.visibility = View.GONE
        }
        if (!record.soundNameSecond.isNullOrBlank() && audioController.isAudioExist(record.soundNameSecond!!)) {
            layoutSoundSecond.visibility = View.VISIBLE

            imgSoundPlaySecond.setOnClickListener {
                if (!secondSoundRunning) {
                    audioController.startPlay(record.soundNameSecond!!)
                    imgSoundPlaySecond.setImageResource(R.drawable.ic_stop)
                    secondSoundRunning = true
                    lineSecond.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDarkRed))
                    Log.d(com.orange_infinity.gratitude.TAG, "Second music start playing")
                } else {
                    audioController.stopPlay()
                    imgSoundPlaySecond.setImageResource(R.drawable.ic_play)
                    secondSoundRunning = false
                    lineSecond.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMusicLine))
                    Log.d(com.orange_infinity.gratitude.TAG, "Second music stop playing")
                }
            }
        } else {
            layoutSoundSecond.visibility = View.GONE
        }
    }

    private fun setUpImages() {
        val pxLayoutHeight = convertDpToPixel(this, 300)
        if (!record.imageName.isNullOrBlank()) {
            ImageLoader(record.imageName!!, this, pxLayoutHeight, pxLayoutHeight).execute(imgFirst)
            imgFirst.setOnClickListener {
                //                val intent = Intent(this, ImageViewerActivity::class.java)
//                intent.putExtra(IMAGE_NAME_KEY, record.imageName)
//                startActivity(intent)
            }
        } else {
            imgFirst.visibility = View.INVISIBLE
        }

        if (!record.imageNameSecond.isNullOrBlank()) {
            ImageLoader(record.imageNameSecond!!, this, pxLayoutHeight, pxLayoutHeight).execute(imgSecond)
            imgSecond.setOnClickListener {
                //                val intent = Intent(this, ImageViewerActivity::class.java)
//                intent.putExtra(IMAGE_NAME_KEY, record.imageNameSecond)
//                startActivity(intent)
            }
        } else {
            imgSecond.visibility = View.INVISIBLE
        }
    }

    override fun onLoadComplete() {
    }
}
