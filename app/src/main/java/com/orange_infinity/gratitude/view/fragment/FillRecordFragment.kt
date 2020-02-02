package com.orange_infinity.gratitude.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordButton
import com.devlomi.record_view.RecordView
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.TAG
import com.orange_infinity.gratitude.model.database.entities.Record
import com.orange_infinity.gratitude.saveImageToGallery
import com.orange_infinity.gratitude.useCase.AudioController
import com.orange_infinity.gratitude.useCase.IMAGE_MINI
import com.orange_infinity.gratitude.useCase.RecordEntityManager
import kotlinx.android.synthetic.main.fiil_record_fragment.*
import kotlinx.android.synthetic.main.fiil_record_fragment.view.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


const val GALLERY_REQUEST = 1
const val REQUEST_RECORD_AUDIO = 2

class FillRecordFragment : Fragment() {

    private lateinit var activity: Activity
    private lateinit var recordEntityManager: RecordEntityManager
    private val audioRecorder = AudioController()
    private var countOfRecords: Int = 1
    private var isTop = true
    private var recordBitmap: Bitmap? = null
    private var scaledBitmap: Bitmap? = null
    private var soundName: String? = null
    private var isRecordAudio = false
    private lateinit var recordButton: RecordButton
    private lateinit var recordView: RecordView

    companion object {
        fun newInstance(activity: Activity, countOfRecords: Int, isTop: Boolean): FillRecordFragment {
            val instance = FillRecordFragment()
            instance.activity = activity
            instance.countOfRecords = countOfRecords
            instance.isTop = isTop
            instance.recordEntityManager = RecordEntityManager(activity)

            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fiil_record_fragment, container, false)

        when {
            countOfRecords < 3 -> createFirstLevelText(v)
            countOfRecords < 6 -> createSecondLevelText(v)
            else -> createFreeLevel(v)
        }

        recordButton = v.findViewById(R.id.imgMicrophone)
        recordView = v.findViewById(R.id.recordView)
        recordButton.setRecordView(recordView)

        recordView.cancelBounds = 160f // dp

        v.recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                //Start Recording..
                if (!hasPermissions(activity)) {
                    requestPermission(activity)
                } else {
                    recordSound()
                    v.editRecord.isEnabled = false
                }
                Log.i(TAG, "onStart")
            }

            override fun onCancel() {
                //On Swipe To Cancel
                Log.i(TAG, "onCancel")
                audioRecorder.deleteAudio(soundName ?: return)
                soundName = null
                v.editRecord.isEnabled = true
            }

            override fun onFinish(recordTime: Long) {
                //Stop Recording..
                //val time = getHumanTimeText(recordTime)
                Log.i(TAG, "onFinish")
                v.editRecord.isEnabled = true
                //Log.d("RecordTime", time)
            }

            override fun onLessThanSecond() {
                //When the record time is less than One Second
                Log.i(TAG, "onLessThanSecond")
                audioRecorder.deleteAudio(soundName ?: return)
                soundName = null
                v.editRecord.isEnabled = true
            }
        })

        v.imgPaint.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }

        v.imgLoaded.setOnClickListener {
            imgLoaded.visibility = View.GONE
            recordBitmap = null
        }

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataIntent)

        if (requestCode == GALLERY_REQUEST) {
            val selectedImage = dataIntent?.data ?: return

            try {
                recordBitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, selectedImage)
                val width = recordBitmap!!.width
                val height = recordBitmap!!.height
                val divider = (width / 180).coerceAtMost(height / 180)
                scaledBitmap = Bitmap.createScaledBitmap(recordBitmap, width / divider, height / divider, false)

                imgLoaded.setImageBitmap(scaledBitmap)
                imgLoaded.visibility = View.VISIBLE
            } catch (ex: IOException) {
            }
        }
    }

    override fun onDestroyView() {
        saveRecord()
        audioRecorder.releaseRecorder()
        super.onDestroyView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (hasPermissions(activity)) {
                //recordSound()
            } else {
                //requestPermission(activity)
            }
        }
    }

    private fun requestPermission(context: Activity) {
        if (!hasPermissions(context)) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO
            )
        } else {
            //recordSound()
        }
    }

    private fun hasPermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun recordSound() {
        if (!isRecordAudio) {
            soundName = UUID.randomUUID().toString()
            audioRecorder.recordStart(soundName!!)
            isRecordAudio = true
        } else {
            audioRecorder.recordStop()
            isRecordAudio = false
        }
    }

    private fun saveRecord() {
        val text = editRecord.text.toString()
        if (text.isNotEmpty()) {
            //SystemPreferences.saveBoolean(activity, IS_JOURNAL_NOT_EMPTY, true)
            var imageName = ""

            if (recordBitmap != null) {
                imageName = UUID.randomUUID().toString()
                saveImageToGallery(recordBitmap!!, imageName)
                saveImageToGallery(scaledBitmap!!, imageName + IMAGE_MINI)
            }

            saveNoticing(text, imageName, soundName)
            //checkNewLevel()
        } else if (!soundName.isNullOrBlank()) {
            audioRecorder.deleteAudio(soundName!!)
        }
    }

    private fun createFirstLevelText(v: View) {
        if (isTop) {
            v.tvTitle.text = "Did anything good happened to you today?"
            v.tvDescription.text = "Met a friend for a lunch? Had a moment of calm?\\n Nothing is too big or too small!"
        } else {
            v.tvTitle.text = "What could you be grateful for today?"
            v.tvDescription.text = "Good weather? Cup of coffee? A loved one?"
            //v.imgMicrophone.setImageResource(R.drawable.microphone)
            v.imgPaint.setImageResource(R.drawable.paint)
        }
    }

    private fun createSecondLevelText(v: View) {
        if (isTop) {
            v.tvTitle.text = "What would you be missing from your life?"
            v.tvDescription.text = "Are there things that you usually don't appreciate? It can be anything from the running water to your partner."
        } else {
            v.tvTitle.text = "What could you be grateful for today?"
            v.tvDescription.text = "Good weather? Cup of coffee? A loved one?"
            //v.imgMicrophone.setImageResource(R.drawable.microphone)
            v.imgPaint.setImageResource(R.drawable.paint)
        }
    }

    private fun createFreeLevel(v: View) {
        if (isTop) {
            v.tvTitle.text = "WHAT ARE YOU GRATEFUL FOR TODAY?"
            v.tvDescription.text = ""
        } else {
            v.tvTitle.text = "WHAT ARE YOU GRATEFUL FOR IN YOUR LIFE?"
            v.tvDescription.text = ""
            //v.imgMicrophone.setImageResource(R.drawable.microphone)
            v.imgPaint.setImageResource(R.drawable.paint)
        }
    }

    private fun checkNewLevel() {
//        countOfRecords++
//        if (countOfRecords % 3 == 0) {
//            startNewLevelWithCitation(countOfRecords / 3)
//        }
    }

    private fun startNewLevelWithCitation(level: Int) {
//        if (level == 1) { // set up level 2
//            RecordCountPreferences.saveRecordCount(activity, 2)
//            val intent = Intent(activity, CitationActivity::class.java)
//            intent.putExtra(IMAGE_R_ID_KEY, R.drawable.end_one)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startActivity(intent)
//        } else if (level == 2) { // set up level 3 (free)
//            RecordCountPreferences.saveRecordCount(activity, 3)
//            val intent = Intent(activity, CitationActivity::class.java)
//            intent.putExtra(IMAGE_R_ID_KEY, R.drawable.level3)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startActivity(intent)
//        } else {    // set up??
//            RecordCountPreferences.saveRecordCount(activity, 4)
//        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun saveNoticing(description: String, imageName: String, soundName: String?) {
        object : AsyncTask<Unit, Unit, Unit>() {

            override fun doInBackground(vararg params: Unit) {
                val record = Record()
                val formatForDateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                val currentDate = formatForDateNow.format(Date())
                record.date = currentDate
                record.description = description
                record.imageName = imageName
                record.soundName = soundName

                //AppDatabase.getInstance(activity).getRecordDao().insert(record)
                recordEntityManager.saveRecord(record)
            }

        }.execute()
    }
}