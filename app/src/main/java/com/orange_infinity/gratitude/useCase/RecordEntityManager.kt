package com.orange_infinity.gratitude.useCase

import android.app.Activity
import com.orange_infinity.gratitude.model.database.entities.Record

class RecordEntityManager {

    fun save(activity: Activity, record: Record) {
        Thread {
            RecordEntityService(activity).saveRecord(record)
        }.start()
    }
}