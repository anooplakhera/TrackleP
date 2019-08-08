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

    @POST(ApiUrls.BILLING_DETAILS)
    fun getBillingDashboard(@Header(ApiUrls.Authorization) auth: String, @Body bodyMap: RequestBody): Call<ResponseModelClasses.BillingDashboardResponse>

    @POST(ApiUrls.CONNECT_UTILITY + "/{AccountNumber}")
    fun getConnectWithUtility(@Header(ApiUrls.Authorization) auth: String, @Path("AccountNumber") id: String, @Body bodyMap: RequestBody): Call<ResponseModelClasses.ConnectWithUtilityResponse>

    @POST(ApiUrls.UsageNotification + "/{AccountNumber}")
    fun getUsageNotification(@Header(ApiUrls.Authorization) auth: String, @Path("AccountNumber") id: String, @Body bodyMap: RequestBody): Call<ArrayList<ResponseModelClasses.GetUsageNotificationResponse>>

    @FormUrlEncoded
    @POST(ApiUrls.FORGET + ApiUrls.STEP_ONE)
    fun getResetUserPass1(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.ResetPassStep1Response>

    @FormUrlEncoded
    @POST(ApiUrls.FORGET + ApiUrls.STEP_TWO)
    fun getResetUserPass2(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.ResetPassStep2Response>

    @POST(ApiUrls.Account + "/{AccountNumber}")
    fun getAccount(@Header(ApiUrls.Authorization) auth: String, @Path("AccountNumber") id: String, @Body bodyMap: RequestBody): Call<ArrayList<ResponseModelClasses.MyProfile>>

    //    @Headers(ApiUrls.Authorization + ":" + ApiUrls.AuthKey)
    @GET(ApiUrls.GET_UTILS)
    fun getUtilityList(/*@Header(ApiUrls.Authorization) Auth: String*/): Call<ResponseModelClasses.UtilityListResponseModel>

    @POST(ApiUrls.UpdateAccount)
    fun getUpdateAccount(@Header(ApiUrls.Authorization) Auth: String, @Body bodyMap: RequestBody): Call<ResponseModelClasses.UpdateProfile>

    @POST(ApiUrls.MeterDetails + "/{AccountNumber}")
    fun getMeterDetails(@Header(ApiUrls.Authorization) auth: String, @Body bodyMap: RequestBody, @Path("AccountNumber") value: String): Call<ResponseModelClasses.MeterDetails>

    @POST(ApiUrls.WaterUsages)
    fun getWaterUsages(@Header(ApiUrls.Authorization) auth: String, @Body bodyMap: RequestBody): Call<ResponseModelClasses.WaterUsages>

    @POST(ApiUrls.CompareSpending + "/{AccountNumber}" + "/{unit}")
    fun getCompareSpendingDetails(
        @Header(ApiUrls.Authorization) auth: String, @Header("unit") unit: String, @Body bodyMap: RequestBody, @Path(
            "AccountNumber"
        ) value: String
    ): Call<ResponseModelClasses.CompareDataResponse>


    @FormUrlEncoded
    @POST(ApiUrls.AccountAdd)
    fun setAddAccount(@Header(ApiUrls.Authorization) auth: String, @FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.AddAccount>


    @POST(ApiUrls.UsageNotification)
    fun setUsageNotification(@Header(ApiUrls.Authorization) auth: String, @Body bodyMap: RequestBody): Call<ResponseModelClasses.UpdateUsageNotification>


}