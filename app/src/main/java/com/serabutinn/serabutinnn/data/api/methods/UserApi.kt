package com.serabutinn.serabutinnn.data.api.methods
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
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import com.serabutinn.serabutinnn.data.api.response.TakeJobResponse
import com.serabutinn.serabutinnn.data.api.response.TitleCheckResponse
import com.serabutinn.serabutinnn.data.api.response.UpdateBioResponse
import com.serabutinn.serabutinnn.data.api.response.UpdateJobsResponse
import com.serabutinn.serabutinnn.data.api.response.VerifyFaceResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
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
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Call<UpdateBioResponse>

    @FormUrlEncoded
    @POST("/customer/validate/jobs")
    fun checkTitle(
        @Header("Authorization") token: String,
        @Field("title") title: String
    ):Call<TitleCheckResponse>

    @Multipart
    @POST("/customer/upload-face")
    fun uploadFace(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part?
    ): Call<SignupResponse>

    @Multipart
    @POST("/customer/verify-face")
    fun verifyFace(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part?
    ): Call<VerifyFaceResponse>

    @Multipart
    @POST("/customer/jobs/create")
    fun createJob(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("deadline") deadline: RequestBody,
        @Part("cost") price: RequestBody,
        @Part("location") location: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<CreateJobsResponse>

    @FormUrlEncoded
    @POST("/customer/payment/create")
    fun createPayment(
        @Header("Authorization") token: String,
        @Field("job_id") jobId:String
    ): Call<CreatePaymentResponse>

    @POST("/customer/jobs/update/{job_id}")
    fun updateJob(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("deadline") deadline: RequestBody,
        @Part("cost") price: RequestBody,
        @Part("location") location: RequestBody,
        @Part image: MultipartBody.Part?,
        @Path("job_id") jobId: String
    ): Call<UpdateJobsResponse>

    @DELETE("/customer/jobs/delete/{job_id}")
    fun deleteJob(
        @Header("Authorization") token: String,
        @Path("job_id") jobId: String
    ): Call<DeleteJobsResponse>

    @GET("/customer/jobs")
    fun getCustomerJobs(
        @Header("Authorization") token: String
    ): Call<ListCustomerJobsResponse>

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
        @Header("Authorization") token: String
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