package com.serabutinn.serabutinnn.repository

import com.serabutinn.serabutinnn.data.local.Job
import com.serabutinn.serabutinnn.data.local.JobDao

class JobRepository(private val jobDao: JobDao) {

    suspend fun insertJob(job: Job) {
        jobDao.insertJob(job)
    }

    suspend fun getJobById(jobId: String): Job? {
        return jobDao.getJobById(jobId)
    }
}