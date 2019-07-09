package com.example.tracklep.ApiClient

import com.example.tracklep.DataModels.ResponseModelClasses
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiUrls.LOGIN)
    fun getLogin(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.LoginResponseModel>

    @FormUrlEncoded
    @POST(ApiUrls.REGISTER_USER)
    fun registerUser(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.RegistrationResponse>

    @POST(ApiUrls.SECURITY_QUESTION)
    fun getSecurityQuestion(@Body bodyMap:  String): Call<ArrayList<ResponseModelClasses.SecurityQuestionResponse>>

    @FormUrlEncoded
    @POST(ApiUrls.FORGET + ApiUrls.STEP_ONE)
    fun getResetUserPass1(@Field(ApiUrls.UserName) UserName: String): Call<ResponseModelClasses.ResetPassStep1Response>

    @FormUrlEncoded
    @POST(ApiUrls.FORGET + ApiUrls.STEP_TWO)
    fun getResetUserPass2(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.ResetPassStep2Response>

    @GET(ApiUrls.Account + "{id}")
    fun getAccount(@Header(ApiUrls.Authorization) Auth: String): Call<String>

    @POST(ApiUrls.UpdateAccount)
    fun getupdateAccount(@Header(ApiUrls.Authorization) Auth: String, @FieldMap fieldMap: Map<String, String>): Call<String>


}