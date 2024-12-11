package com.serabutinn.serabutinnn.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_table")
data class Job(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val job_id: String,
    val payment_link: String,
)
