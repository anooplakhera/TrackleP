package com.example.tracklep.ApiClient

import com.example.tracklep.DataModels.ResponseModelClasses
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiUrls.LOGIN)
    fun getLogin(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.LoginResponseModel>

    @FormUrlEncoded
    @POST(ApiUrls.REGISTER_USER)
    fun registerUser(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.RegistrationResponse>

    @POST(ApiUrls.SECURITY_QUESTION)
    fun getSecurityQuestion(@Body bodyMap: RequestBody): Call<ArrayList<ResponseModelClasses.SecurityQuestionResponse>>

    //Billing Dashboard
    @POST(ApiUrls.BILLING_DETAILS)
    fun getBillingDashboard(@Body bodyMap: RequestBody): Call<ArrayList<ResponseModelClasses.BillingDashboardResponse>>

    @POST(ApiUrls.SECURITY_QUESTION)
    fun getSecurityQuestion1(@Body bodyMap: String): Call<String>

    @FormUrlEncoded
    @POST(ApiUrls.FORGET + ApiUrls.STEP_ONE)
    fun getResetUserPass1(@Field(ApiUrls.UserName) UserName: String): Call<ResponseModelClasses.ResetPassStep1Response>

    @FormUrlEncoded
    @POST(ApiUrls.FORGET + ApiUrls.STEP_TWO)
    fun getResetUserPass2(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.ResetPassStep2Response>

    @Headers(ApiUrls.Authorization + ":" + ApiUrls.AuthKey)
    @GET(ApiUrls.Account + "/7")
    fun getAccount(@Field(ApiUrls.Account) UserName: String): Call<ResponseModelClasses.MyProfileResponse>

    @Headers(ApiUrls.Authorization + ":" + ApiUrls.AuthKey)
    @GET(ApiUrls.GET_UTILS)
    fun getUtilityList(/*@Header(ApiUrls.Authorization) Auth: String*/): Call<ResponseModelClasses.UtilityListResponseModel>

    @POST(ApiUrls.UpdateAccount)
    fun getupdateAccount(@Header(ApiUrls.Authorization) Auth: String, @FieldMap fieldMap: Map<String, String>): Call<String>


}