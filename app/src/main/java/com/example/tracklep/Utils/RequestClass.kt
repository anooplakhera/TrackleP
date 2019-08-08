package com.example.tracklep.Utils

import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.DataClasses.ResetPassSecurityQuestionData
import com.example.tracklep.TrackleApp
import com.google.gson.Gson

object RequestClass {

    fun getLoginRequestModel(username: String, password: String): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.UserName, username);
        map.put(ApiUrls.Password, password);
        map.put(ApiUrls.GrantType, ApiUrls.Password.toLowerCase());
        map.put(ApiUrls.TanentId, "1");
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, ApiUrls.DBPassword_value)
        AppLog.printLog("getLoginRequestModel: " + Gson().toJson(map))
        return map;
    }

    fun getMeterDetailsRequestModel(AccountNumber: String): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.AccountNumber, AccountNumber)
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, "aquatrax")//ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, "newaqt@123")//ApiUrls.DBPassword_value)
        AppLog.printLog("getMeterDetailsRequestModel: " + Gson().toJson(map))
        return map;
    }

    fun getWaterUsageRequestModel(AccountNumber: String): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.AccountNumber, AccountNumber)
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, "aquatrax")//ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, "newaqt@123")//ApiUrls.DBPassword_value)
        map.put("Type", "G")//ApiUrls.DBPassword_value)
        map.put("HourlyType", "H")//ApiUrls.DBPassword_value)
        map.put("Mode", "B")//ApiUrls.DBPassword_value)
        map.put("Year", "0")//ApiUrls.DBPassword_value)
        AppLog.printLog("getWaterUsageRequestModel: " + Gson().toJson(map))

        return map;
    }

    fun getBillingDetailsRequestModel(AccountNumber: String): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.AccountNumber, AccountNumber)
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, "aquatrax")//ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, "newaqt@123")//ApiUrls.DBPassword_value)

        AppLog.printLog("getBillingDetailsRequestModel: " + Gson().toJson(map))

        return map;
    }

    //Connect With Utility
    fun getConnectWithUtilityRequestModel(): HashMap<String, String> {
        var map = HashMap<String, String>()
        //map.put(ApiUrls.AccountNumber, AccountNumber)
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, "aquatrax")//ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, "newaqt@123")//ApiUrls.DBPassword_value)

        AppLog.printLog("getConnectWithUtilityRequestModel: " + Gson().toJson(map))

        return map;
    }

    fun getSignupRequestModel(
        email: String,
        password: String,
        utilityID: String,
        editAccountNo: String,
        editServiceZipCode: String,
        editMeterNo: String,
        editAnswer1: String,
        editAnswer2: String,
        sQuesID1: String,
        sQuesID2: String,
        cPassword: String
    ): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.EmailID, email)
        map.put(ApiUrls.Password, password)
        map.put(ApiUrls.CustomerUtilityId, utilityID)
        map.put(ApiUrls.ConfirmPassword, cPassword)
        map.put(ApiUrls.UtilityAccountNumber, editAccountNo)
        map.put(ApiUrls.PostalCode, editServiceZipCode)
        map.put(ApiUrls.UserId, email)
        map.put(ApiUrls.MeterNumber, editMeterNo)
        map.put(ApiUrls.SecurityQuestionId, sQuesID1)
        map.put(ApiUrls.SecurityQuestionId2, sQuesID2)
        map.put(ApiUrls.HintsAns, editAnswer1)
        map.put(ApiUrls.HintsAns2, editAnswer2)
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, ApiUrls.DBPassword_value)
        AppLog.printLog("getSignupRequestModel: " + Gson().toJson(map))
        return map;
    }

    fun getUpdateAccountRequestModel(
        email: String,
        HomePhone: String,
        MobilePhone: String,
        Custid: String,
        AccountNumber: String,
        editAnswer1: String,
        editAnswer2: String,
        sQuesID1: String,
        sQuesID2: String
    ): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.EmailID, email)
        map.put(ApiUrls.HomePhone, HomePhone)
        map.put(ApiUrls.MobilePhone, MobilePhone)
        map.put(ApiUrls.Custid, Custid)
        map.put(ApiUrls.AccountNumber, AccountNumber)
        map.put(ApiUrls.SecurityQuestionId, sQuesID1)
        map.put(ApiUrls.SecurityQuestionId2, sQuesID2)
        map.put(ApiUrls.HintsAns, editAnswer1)
        map.put(ApiUrls.HintsAns2, editAnswer2)
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, ApiUrls.DBPassword_value)
        AppLog.printLog("getUpdateAccountRequestModel: " + Gson().toJson(map))
        return map;
    }

    fun getForgetRequestStepOne(username: String): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.UserName, username);
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, ApiUrls.DBPassword_value)
        AppLog.printLog("getForgetRequestStepOne: " + Gson().toJson(map))
        return map;
    }

    fun getForgetRequestStepTwo(username: String, editAnswer1RP: String, editAnswer2RP: String): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.UserName, username);
        map.put(ApiUrls.SecurityQuestion1, ResetPassSecurityQuestionData.getArrayItem(0).SecurityQuestionId);
        map.put(ApiUrls.SecurityQuestion2, ResetPassSecurityQuestionData.getArrayItem(1).SecurityQuestionId);
        map.put(ApiUrls.Answer1, editAnswer1RP)
        map.put(ApiUrls.Answer2, editAnswer2RP)
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, ApiUrls.DBPassword_value)
        AppLog.printLog("getForgetRequestStepTwo: " + Gson().toJson(map))
        return map;
    }


    /*["Database": "AQUATRAXDEV", "MobileNumber":"1234567843", "DataSource":
"aquatraxserver.database.windows.net",
"PostalCode": "21414", "CustomerId": "7",
"UtilityAccountNumber": "3424231", "MeterNumber":
"421142142", "EmailId": "u.agarwal8@gmail.com",
"UtilityId": "0", "DBUserName": "aquatrax",
"DBPassword": "newaqt@123"]*/
    fun getAddAccountRequestModel(
        UtilityAccountNumber: String,
        PostalCode: String,
        MeterNumber: String
    ): Map<String, String> {
        var map = HashMap<String, String>()

        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.DBUserName, ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, ApiUrls.DBPassword_value)
        map.put(
            "MobileNumber",
            TrackleApp.appContext?.let { AppPrefences.getProfileInfo(it).MobilePhone }.toString()
        )
        map.put("PostalCode", PostalCode)
        map.put("CustomerId", TrackleApp.appContext?.let { AppPrefences.getProfileInfo(it).CustomerId }.toString())
        map.put("EmailId", TrackleApp.appContext?.let { AppPrefences.getProfileInfo(it).EmailId }.toString())
        map.put(ApiUrls.UtilityAccountNumber, UtilityAccountNumber)
        map.put("UtilityId", "0")
        map.put(ApiUrls.MeterNumber, MeterNumber)

        AppLog.printLog("getAddAccountRequestModel: " + Gson().toJson(map))
        return map;
    }
}