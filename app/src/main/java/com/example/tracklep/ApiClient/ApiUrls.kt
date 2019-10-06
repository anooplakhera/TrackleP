package com.example.tracklep.ApiClient

import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.Utils.AppLog
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.util.*

object ApiUrls {


    /*-----------------Api+Urls----------------*/

    //    const val BASE_URL: String = "https://aquatraxapi-dev.azurewebsites.net/"
    const val BASE_URL: String = "https://aquatraxportalprod-api.azurewebsites.net/"
    const val BASE_PATH: String = "api/customer/"
    const val LOGIN = "token";
    const val GET_UTILS = "getutilities"
    const val REGISTER_USER = "register"
    const val FORGET = "reset-password/"
    const val STEP_ONE = "step-one"
    const val STEP_TWO = "step-two"
    const val STEP_THREE = "step-three"
    const val SECURITY_QUESTION = "security-questions"
    const val BILLING_DETAILS = "Billing-Details"
    const val Authorization = "authorization"
    const val CONNECT_UTILITY = "getcontactus"
    const val UsageNotification = "leak-water-notification"
    const val Account = "account"
    const val UpdateAccount = "update-account"
    const val MeterDetails = "meter-details"
    const val WaterUsages = "water-usages"
    const val CompareSpending = "water-compare-spending"
    const val AccountDelete = "account/delete"
    const val AuthKey =
        "Bearer KQut6fhk1BLmO8eVIaMteTcljOes4CfuB2QiuqDvp4Gmoy9Yk_k-umomjcq_IndzuyyMTFm5gVLAcWClmJrdks3Uvhwqg-SYNH74AzVpjYMcgdY4WDIdo-EGHktAYrF70KjUzxeWXLVxeQVs2prmeNWZKlYm73DI16ls4dwoAMBVij4hISTcqCnWat9Ou3-8jRt_TWLCQAG7K6Iq_0yAoVaa1AF6sOgBf-wBB8OMx6MtnAdgzIUY4iLnA2iy4EvGdVmmL4LpHmpL92GxJi2BAL8vn3wAzUhhxu1mPRsk6PacOT5oJQq3eWhM-ZyYGLv3V_xnkfBBnkM1cM2RLFeJ6kYY7Jh1sb1mQ0F-3UurErsA9Y95HvEGDJo7kg_o2ZzivnQhbIiZqX8gbmsiWI_d-DjSnWPHO_RtlpIAcM55r_XCHjlKMcAR-TaI_9nhpKmAWi0DyF6ySBK0RT9_OA8J-w";

    /*---------------Params-----------------*/
    /*ResetPass*/
    const val UserName = "UserName"
    const val SecurityQuestion1 = "SecurityQuestion1"
    const val SecurityQuestion2 = "SecurityQuestion2"
    const val Answer1 = "Answer1"
    const val Answer2 = "Answer2"

    /*SignUp*/
    const val EmailID = "EmailID"
    const val Password = "Password"
    const val CustomerUtilityId = "CustomerUtilityId"
    const val ConfirmPassword = "ConfirmPassword"
    const val UtilityAccountNumber = "UtilityAccountNumber"
    const val AccountNumber = "AccountNumber"
    const val PostalCode = "PostalCode"
    const val UserId = "UserId"
    const val MeterNumber = "MeterNumber"
    const val ServiceZipCode = "ServiceZipCode"
    const val SecurityQuestionId = "SecurityQuestionId"
    const val SecurityQuestionId2 = "SecurityQuestionId2"
    const val HintsAns = "HintAns"
    const val HintsAns2 = "HintsAns2"
    const val GrantType = "grant_type"
    const val TanentId = "tanentid"

    //Update Account
    const val HomePhone = "HomePhone"
    const val MobilePhone = "MobilePhone"
    const val Custid = "Custid"
    const val BusinessHomePhone = "BusinessHomePhone"
    const val BillingPreference = "BillingPreference"
    const val FilterDate = "filterdate"

    //Connect Me
    const val TopicID = "TopicID"
    const val Subject = "Subject"
    const val Body = "Body"

    const val DataSource = "DataSource"
    const val Database = "Database"
    const val DBUserName = "DBUserName"
    const val DBPassword = "DBPassword"

//    1-ServerName
//    2-DataBaseName
//    3-UserName
//    4-Password

//    const val DataSource_value = "aquatraxserver.database.windows.net"
//    const val Database_value = "AQUATRAXDEV"
//    const val DBUserName_value = "aquatrax@aquatraxserver"
//    const val DBPassword_value = "newaqt@123"

    const val AccountAdd = "account/add";
    const val SetConnectMe = "SetContactUS";

    fun getBasePathUrl(): String {
        return BASE_URL + BASE_PATH
    }

    fun getBodyMap(data: ResponseModelClasses.DataBaseUtils): HashMap<String, String> {
        var bMap = HashMap<String, String>()
        bMap.put(DataSource, data.ServerName)
        bMap.put(Database, data.DataBaseName)
        bMap.put(DBUserName, data.UserName)
        bMap.put(DBPassword, data.Password)
        AppLog.printLog("getWaterUsageRequestModel: " + Gson().toJson(bMap))
        return bMap
    }

    fun getJSONRequestBody(stringHashMap: HashMap<String, String>?): RequestBody {
        val jsonObject = JSONObject()
        if (stringHashMap != null && stringHashMap.size > 0) {
            try {
                for ((key, value) in stringHashMap) {
                    jsonObject.put(key, value)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            jsonObject.toString()
        )
    }
}