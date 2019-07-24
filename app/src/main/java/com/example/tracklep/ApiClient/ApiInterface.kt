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

    @FormUrlEncoded
    @POST(ApiUrls.FORGET + ApiUrls.STEP_ONE)
    fun getResetUserPass1(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.ResetPassStep1Response>

    @FormUrlEncoded
    @POST(ApiUrls.FORGET + ApiUrls.STEP_TWO)
    fun getResetUserPass2(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.ResetPassStep2Response>

    @POST(ApiUrls.Account + "/{AccountNumber}")
    fun getAccount(@Header(ApiUrls.Authorization) auth: String, @Path("AccountNumber") id: String, @Body bodyMap: RequestBody): Call<List<ResponseModelClasses.MyProfile>>

    //    @Headers(ApiUrls.Authorization + ":" + ApiUrls.AuthKey)
    @GET(ApiUrls.GET_UTILS)
    fun getUtilityList(/*@Header(ApiUrls.Authorization) Auth: String*/): Call<ResponseModelClasses.UtilityListResponseModel>

    @POST(ApiUrls.UpdateAccount)
    fun getUpdateAccount(@Header(ApiUrls.Authorization) Auth: String,  @Body bodyMap: RequestBody): Call<String>

    @POST(ApiUrls.MeterDetails + "/{AccountNumber}")
    fun getMeterDetails(@Header(ApiUrls.Authorization) auth: String, @Body bodyMap: RequestBody, @Path("AccountNumber") value: String): Call<ResponseModelClasses.MeterDetails>

    @POST(ApiUrls.WaterUsages)
    fun getWaterUsages(@Header(ApiUrls.Authorization) auth: String, @Body bodyMap: RequestBody): Call<ResponseModelClasses.WaterUsages>


}