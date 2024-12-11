package com.serabutinn.serabutinnn.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface JobDao {
    @Insert
    suspend fun insertJob(job: Job)

    @Query("SELECT * FROM job_table WHERE job_id = :jobId")
    suspend fun getJobById(jobId: String): Job?
}