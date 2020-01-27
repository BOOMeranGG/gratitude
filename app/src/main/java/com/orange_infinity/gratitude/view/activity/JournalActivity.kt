package com.orange_infinity.gratitude.view.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.database.entities.Record
import com.orange_infinity.gratitude.presenter.ImageLoader
import com.orange_infinity.gratitude.readBitmapFromDisk
import com.orange_infinity.gratitude.view.activity.interfaces.ImageLoaderOwner
import kotlinx.android.synthetic.main.list_record.view.*

class JournalActivity : BaseActivity(), ImageLoaderOwner {

    private lateinit var recordRecycler: RecyclerView
    private var records = mutableListOf<Record>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

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

    private fun updateRecycler() {
        val adapter = recordRecycler.adapter as RecordAdapter
        adapter.records = records
        recordRecycler.adapter!!.notifyDataSetChanged()
    }

    private fun init() {
        recordRecycler = findViewById(R.id.recyclerRecords)
        recordRecycler.layoutManager = GridLayoutManager(this, 1)
        recordRecycler.adapter = RecordAdapter(records)
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

    // -----------------------------------------------------------------------------------------------------------------

    private inner class RecordHolder(val recordView: View) :
        RecyclerView.ViewHolder(recordView), View.OnClickListener {

        fun bindRecord(record: Record, position: Int, countOfRecords: Int) {
            recordView.tvDate.text = record.date
            recordView.tvDescription.text = record.description

            if (position == 0) {
                recordView.layoutTitle.visibility = View.VISIBLE
            }
            if (position == countOfRecords - 1) {
                recordView.line1.visibility = View.GONE
                recordView.line2.visibility = View.GONE
            }

            if (!record.imageName.isNullOrBlank()) {
                ImageLoader(record.imageName!!, this@JournalActivity).execute(recordView.imgRecord)
            }

            recordView.listRecordLayout.setOnClickListener(this)
        }

        override fun onClick(v: View) {
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
