package com.orange_infinity.gratitude.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Record {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var date: String? = null
    var description: String = ""
    var imageName: String? = null
    var soundName: String? = null

    var descriptionSecond: String = ""
    var imageNameSecond: String? = null
    var soundNameSecond: String? = null
}