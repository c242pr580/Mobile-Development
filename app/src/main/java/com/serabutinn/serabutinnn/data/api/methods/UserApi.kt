package com.serabutinn.serabutinnn.data.api.methods

import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.request.CreateJobsRequest
import com.serabutinn.serabutinnn.data.api.request.CreatePaymentRequest
import com.serabutinn.serabutinnn.data.api.request.LoginRequest
import com.serabutinn.serabutinnn.data.api.request.SignupRequest
import com.serabutinn.serabutinnn.data.api.request.UpdateBioRequest
import com.serabutinn.serabutinnn.data.api.response.BiodataResponse
import com.serabutinn.serabutinnn.data.api.response.CompleteJobResponse
import com.serabutinn.serabutinnn.data.api.response.CreateJobsResponse
import com.serabutinn.serabutinnn.data.api.response.CreatePaymentResponse
import com.serabutinn.serabutinnn.data.api.response.DeleteJobsResponse
import com.serabutinn.serabutinnn.data.api.response.ListAllJobsResponse
import com.serabutinn.serabutinnn.data.api.response.ListCustomerJobsResponse
import com.serabutinn.serabutinnn.data.api.response.ListJobsMitraResponse
import com.serabutinn.serabutinnn.data.api.response.ListPendingJobsResponse
import com.serabutinn.serabutinnn.data.api.response.LoginCustResponse
import com.serabutinn.serabutinnn.data.api.response.LoginMitraResponse
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import com.serabutinn.serabutinnn.data.api.response.TakeJobResponse
import com.serabutinn.serabutinnn.data.api.response.UpdateBioResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {

    @POST("/login")
    suspend fun loginCustomer(
        @Body loginRequest: LoginRequest
    ): Response<LoginCustResponse>

    @POST("/login")
    suspend fun loginMitra(
        @Body loginRequest: LoginRequest
    ): Response<LoginMitraResponse>

    @POST("/signup")
    suspend fun signupUser(
        @Body signupRequest: SignupRequest
    ): Response<SignupResponse>

    @GET("/biodata")
    suspend fun getBiodata(
        @Header("Auth") token: String
    ): Response<BiodataResponse>

    @Multipart
    @POST("/biodata/update")
    suspend fun updateBiodata(
        @Body updateBioRequest: UpdateBioRequest,
        @Header("Auth") token: String
    ): Response<UpdateBioResponse>

    @Multipart
    @POST("/customer/jobs/create")
    suspend fun createJob(
        @Header("Auth") token: String,
        @Body createJobRequest: CreateJobsRequest,
        @Part image: MultipartBody.Part
    ): Response<CreateJobsResponse>

    @Multipart
    @POST("/customer/jobs/update/{job_id}")
    suspend fun updateJob(
        @Header("Auth") token: String,
        @Body createJobRequest: CreateJobsRequest,
        @Part image: MultipartBody.Part,
        @Path("job_id") jobId: String
    ): Response<String>

    @DELETE("/customer/jobs/delete/{job_id}")
    suspend fun deleteJob(
        @Header("Auth") token: String,
        @Path("job_id") jobId: String
    ): Response<DeleteJobsResponse>

    @GET("/customer/jobs")
    suspend fun getCustomerJobs(
        @Header("Auth") token: String
    ): Response<ListCustomerJobsResponse>

    @POST("/customer/payment/create")
    suspend fun createPayment(
        @Header("Auth") token: String,
        @Body createJobRequest: CreatePaymentRequest,
        @Part image: MultipartBody.Part
    ): Response<CreatePaymentResponse>

    @GET("/customer/jobs/complete/{job_id}")
    suspend fun completeJob(
        @Header("Auth") token: String,
        @Path("job_id") jobId: String
    ): Response<CompleteJobResponse>

    @POST("/mitra/jobs/assign/{job_id}")
    suspend fun assignJob(
        @Header("Auth") token: String,
        @Path("job_id") jobId: String
    ): Response<TakeJobResponse>

    @GET("/mitra/jobs")
    suspend fun getMitraJobs(
        @Header("Auth") token: String
    ): Response<ListJobsMitraResponse>

    @GET("/alljobs")
    suspend fun getHome(
        @Header("Auth") token: String
    ): Response<ListAllJobsResponse>

    @GET("/jobs/pending")
    suspend fun getPending(
        @Header("Auth") token: String
    ): Response<ListPendingJobsResponse>


    companion object {
        fun getApi(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }
    }
}