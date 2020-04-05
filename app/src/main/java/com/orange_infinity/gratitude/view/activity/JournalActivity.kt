package com.orange_infinity.gratitude.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.database.entities.Record
import com.orange_infinity.gratitude.useCase.AudioController
import com.orange_infinity.gratitude.useCase.IMAGE_MINI
import com.orange_infinity.gratitude.useCase.ImageLoader
import com.orange_infinity.gratitude.view.activity.interfaces.ImageLoaderOwner
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.list_record.view.*


class JournalActivity : BaseActivity(), ImageLoaderOwner {

    private lateinit var recordRecycler: RecyclerView
    private var records = mutableListOf<Record>()
    private val audioController = AudioController()
    private lateinit var layoutRoot: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)
        Slidr.attach(this)

        init()
        getAllNoticing()
    }

    override fun onRestart() {
        super.onRestart()
        recordRecycler.removeAllViews()
        init()
        updateRecycler()
    }

    override fun onLoadComplete() {
    }

    override fun onDestroy() {
        super.onDestroy()
        audioController.stopPlay()
    }

    private fun updateRecycler() {
        val adapter = recordRecycler.adapter as RecordAdapter
        adapter.records = records
        recordRecycler.adapter!!.notifyDataSetChanged()
    }

    private fun init() {
        recordRecycler = findViewById(R.id.recyclerRecords)
        recordRecycler.layoutManager = GridLayoutManager(this, 1)
        recordRecycler.adapter = RecordAdapter(records)

        layoutRoot = findViewById(R.id.layoutRoot)
    }

    @SuppressLint("StaticFieldLeak")
    private fun getAllNoticing() {
        object : AsyncTask<Unit, Unit, List<Record>>() {

            override fun doInBackground(vararg params: Unit): List<Record> {
                return AppDatabase.getInstance(applicationContext).getRecordDao().findAll()
            }

            override fun onPostExecute(allRecords: List<Record>) {
                records = allRecords as MutableList<Record>
                updateRecycler()
            }
        }.execute()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == SOLO_RECORD_RESULT_CODE) {
            val jsonRecord = data?.getStringExtra(RESULT_JSON_RECORD)
            if (!jsonRecord.isNullOrEmpty()) {
                val record = Gson().fromJson(jsonRecord, Record::class.java)
                val oldRecord = records.first { oldRecord ->
                    oldRecord.id == record.id
                }

                records.remove(oldRecord)
                records.add(record)
                records.sortBy {
                    it.id
                }
                getAllNoticing()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    // -----------------------------------------------------------------------------------------------------------------

    private inner class RecordHolder(val recordView: View) :
        RecyclerView.ViewHolder(recordView), View.OnClickListener {

        lateinit var currentRecord: Record

        fun bindRecord(record: Record, position: Int, countOfRecords: Int) {
            currentRecord = record
            setUpTexts(record)
            setUpHeaderAndFooter(position, countOfRecords)
            setUpImages(record)
            setUpSounds(record)
            setViewGoneToNonExistentPart(record)

            recordView.listRecordLayout.setOnClickListener(this)
            //recordView.layoutContent.setOnClickListener {  }
        }

        private fun setViewGoneToNonExistentPart(record: Record) {
            if (record.description.isEmpty()) {
                recordView.imgRecord.visibility = View.GONE
                recordView.imgSound.visibility = View.GONE
            } else if (record.descriptionSecond.isEmpty()) {
                recordView.imgRecordSecond.visibility = View.GONE
                recordView.imgSoundSecond.visibility = View.GONE
            }
        }

        private fun setUpTexts(record: Record) {
            recordView.tvDate.text = record.date
            recordView.tvDescription.text = record.description
            recordView.tvDescriptionSecond.text = record.descriptionSecond

            if (record.description.isEmpty() || record.descriptionSecond.isEmpty()) {
                recordView.separatorDescription.visibility = View.GONE
            }
        }

        private fun setUpSounds(record: Record) {
            if (!record.soundName.isNullOrBlank() && audioController.isAudioExist(record.soundName!!)) {
                recordView.imgSound.setImageResource(R.drawable.ic_sound)
                recordView.imgSound.setOnClickListener {
                    audioController.startPlay(record.soundName!!)
                }
            } else {
                recordView.imgSound.visibility = View.GONE
            }
            if (!record.soundNameSecond.isNullOrBlank() && audioController.isAudioExist(record.soundNameSecond!!)) {
                recordView.imgSoundSecond.setImageResource(R.drawable.ic_sound)
                recordView.imgSoundSecond.setOnClickListener {
                    audioController.startPlay(record.soundNameSecond!!)
                }
            } else {
                recordView.imgSoundSecond.visibility = View.GONE
            }
        }

        private fun setUpImages(record: Record) {
            if (!record.imageName.isNullOrBlank()) {
                ImageLoader(record.imageName!! + IMAGE_MINI, this@JournalActivity).execute(recordView.imgRecord)
                recordView.imgRecord.setOnClickListener {
                    //showImage(record.imageName!!)
                }
            } else {
                recordView.imgRecord.visibility = View.GONE
            }
            if (!record.imageNameSecond.isNullOrBlank()) {
                ImageLoader(record.imageNameSecond!! + IMAGE_MINI, this@JournalActivity)
                    .execute(recordView.imgRecordSecond)
                recordView.imgRecordSecond.setOnClickListener {
                    //showImage(record.imageNameSecond!!)
                }
            } else {
                recordView.imgRecordSecond.visibility = View.GONE
            }
        }

        private fun setUpHeaderAndFooter(position: Int, countOfRecords: Int) {
            if (position == 0) {
                recordView.layoutTitle.visibility = View.VISIBLE
            }
            if (position == countOfRecords - 1) {
                recordView.line1.visibility = View.GONE
                recordView.line2.visibility = View.GONE
            }
        }

        private fun showImage(imageName: String) {
//            val intent = Intent(this@JournalActivity, ImageViewerActivity::class.java)
//            intent.putExtra(IMAGE_NAME_KEY, imageName)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startActivity(intent)
        }

        override fun onClick(v: View) {
            val intent = Intent(this@JournalActivity, SoloRecordActivity::class.java)
            val recordJson = Gson().toJson(currentRecord)
            intent.putExtra(RECORD_JSON_VALUE_KEY, recordJson)
            startActivityForResult(intent, SOLO_RECORD_RESULT_CODE)
        }
    }

    private inner class RecordAdapter(var records: List<Record>) : RecyclerView.Adapter<RecordHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
            val inflater = LayoutInflater.from(this@JournalActivity)
            val view = inflater.inflate(R.layout.list_record, parent, false)

            return RecordHolder(view)
        }

        override fun onBindViewHolder(recordHolder: RecordHolder, position: Int) {
            val record = records[position]
            recordHolder.bindRecord(record, position, records.size)
        }

        override fun getItemCount(): Int = records.size
    }
}
