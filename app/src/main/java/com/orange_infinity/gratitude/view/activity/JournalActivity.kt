package com.orange_infinity.gratitude.view.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orange_infinity.gratitude.R
import com.orange_infinity.gratitude.model.database.AppDatabase
import com.orange_infinity.gratitude.model.database.entities.Record
import kotlinx.android.synthetic.main.list_record.view.*

class JournalActivity : BaseActivity() {

    private lateinit var recordRecycler: RecyclerView
    private var records = mutableListOf<Record>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        //getAllNoticing()
//        btnCreateNoticing.setOnClickListener {
//            saveNoticing()
//        }

        init()
        getAllNoticing()
    }

    override fun onRestart() {
        super.onRestart()
        recordRecycler.removeAllViews()
        init()
        updateRecycler()
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
    private fun saveNoticing() {
        object : AsyncTask<Unit, Unit, Unit>() {

            override fun doInBackground(vararg params: Unit) {
//                val record = Record()
//                val formatForDateNow = SimpleDateFormat("MM/dd/yyyy", Locale.US)
//                val currentDate = formatForDateNow.format(Date())
//                record.date = currentDate
//                record.description = "testDescription created on: $currentDate"
//
//                AppDatabase.getInstance(applicationContext).getRecordDao().insert(record)
            }

        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    private fun getAllNoticing() {
        object : AsyncTask<Unit, Unit, List<Record>>() {

            override fun doInBackground(vararg params: Unit): List<Record> {
                return AppDatabase.getInstance(applicationContext).getRecordDao().findAll()
            }

            override fun onPostExecute(allRecords: List<Record>) {
                records = allRecords as MutableList<Record>
                //init()
                updateRecycler()
//                tvNoticing.text = ""
//                allRecords.forEach { record ->
//                    tvNoticing.text = "${record.id}.) ${record.description} ${tvNoticing.text}\n"
//                }
            }
        }.execute()
    }

    // -----------------------------------------------------------------------------------------------------------------

    private inner class RecordHolder(val recordView: View) :
        RecyclerView.ViewHolder(recordView), View.OnClickListener {

        fun bindRecord(record: Record) {
            recordView.tvDate.text = record.date
            recordView.tvDescription.text = record.description

            recordView.listRecordLayout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
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
            recordHolder.bindRecord(record)
        }

        override fun getItemCount(): Int = records.size
    }
}
