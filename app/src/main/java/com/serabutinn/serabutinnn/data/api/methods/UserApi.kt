package com.serabutinn.serabutinnn.data.api.methods
import com.google.gson.annotations.SerializedName
import com.serabutinn.serabutinnn.data.api.request.CreateJobsRequest
import com.serabutinn.serabutinnn.data.api.request.CreatePaymentRequest
import com.serabutinn.serabutinnn.data.api.request.UpdateBioRequest
import com.serabutinn.serabutinnn.data.api.response.BiodataResponse
import com.serabutinn.serabutinnn.data.api.response.CompleteJobResponse
import com.serabutinn.serabutinnn.data.api.response.CreateJobsResponse
import com.serabutinn.serabutinnn.data.api.response.CreatePaymentResponse
import com.serabutinn.serabutinnn.data.api.response.DeleteJobsResponse
import com.serabutinn.serabutinnn.data.api.response.DetailJobResponse
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
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {

    @POST("/login")
    @FormUrlEncoded
    fun loginCustomer(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginCustResponse>

    @POST("/login")
    fun loginMitra(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginMitraResponse>


    @POST("/register")
    @FormUrlEncoded
    fun signupUser(
        @Field("username") username: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("location") location: String,
        @Field("role_id") roleid: String
    ): Call<SignupResponse>

    @GET("/biodata")
    fun getBiodata(
        @Header("Authorization") token: String
    ): Call<BiodataResponse>

    @Multipart
    @POST("/biodata/update")
    fun updateBiodata(
        @Body updateBioRequest: UpdateBioRequest,
        @Header("Authorization") token: String
    ): Call<UpdateBioResponse>

    @Multipart
    @POST("/customer/jobs/create")
    fun createJob(
        @Header("Authorization") token: String,
        @Part("title") title: String,
        @Part("description") description: String,
        @Part("deadline") deadline: String,
        @Part("cost") price: Int,
        @Part("location") location: String,
        @Part image: MultipartBody.Part?
    ): Call<CreateJobsResponse>

    @Multipart
    @POST("/customer/jobs/update/{job_id}")
    fun updateJob(
        @Header("Authorization") token: String,
        @Body createJobRequest: CreateJobsRequest,
        @Part image: MultipartBody.Part,
        @Path("job_id") jobId: String
    ): Call<String>

    @DELETE("/customer/jobs/delete/{job_id}")
    fun deleteJob(
        @Header("Authorization") token: String,
        @Path("job_id") jobId: String
    ): Call<DeleteJobsResponse>

    @GET("/customer/jobs")
    fun getCustomerJobs(
        @Header("Authorization") token: String
    ): Call<ListCustomerJobsResponse>

    @POST("/customer/payment/create")
    fun createPayment(
        @Header("Authorization") token: String,
        @Body createJobRequest: CreatePaymentRequest,
        @Part image: MultipartBody.Part
    ): Call<CreatePaymentResponse>

    @GET("/customer/jobs/complete/{job_id}")
    fun completeJob(
        @Header("Authorization") token: String,
        @Path("job_id") jobId: String
    ): Call<CompleteJobResponse>

    @POST("/mitra/jobs/assign/{job_id}")
    fun assignJob(
        @Header("Authorization") token: String,
        @Path("job_id") jobId: String
    ): Call<TakeJobResponse>

    @GET("/mitra/jobs")
    fun getMitraJobs(
        @Header("Authorization") token: String
    ): Call<ListJobsMitraResponse>

    @GET("/alljobs")
    fun getHome(
        @Header("Authentication") token: String
    ): Call<ListAllJobsResponse>

    @GET("/jobs/pending")
    fun getPending(
        @Header("Authorization") token: String
    ): Call<ListPendingJobsResponse>

    @GET("/jobs/detail/{job_id}")
    fun getDetail(
        @Header("Authorization") token: String,
        @Path("job_id") jobId: String
    ):Call<DetailJobResponse>

}