package com.example.tracklep.Utils

import com.example.hp.togelresultapp.Preferences.AppPrefences
import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.DataClasses.ResetPassSecurityQuestionData
import com.example.tracklep.DataModels.ResponseModelClasses
import com.example.tracklep.TrackleApp
import com.google.gson.Gson

object RequestClass {


    fun getLoginRequestModel(
        username: String,
        password: String,
        tanentId: String,
        data: ResponseModelClasses.DataBaseUtils
    ): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.UserName, username)
        map.put(ApiUrls.Password, password)
        map.put(ApiUrls.GrantType, ApiUrls.Password.toLowerCase())
        map.put(ApiUrls.TanentId, "1")
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        AppLog.printLog("getLoginRequestModel: " + Gson().toJson(map))
        return map;
    }

    fun getMeterDetailsRequestModel(
        AccountNumber: String,
        data: ResponseModelClasses.DataBaseUtils
    ): HashMap<String, String> {
        var map = HashMap<String, String>()
        map[ApiUrls.AccountNumber] = AccountNumber
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        AppLog.printLog("getMeterDetailsRequestModel: " + Gson().toJson(map))
        return map;
    }

    fun getWaterUsageRequestModel(
        AccountNumber: String,
        type: String,
        mode: String, data: ResponseModelClasses.DataBaseUtils
    ): HashMap<String, String> {
        var map = HashMap<String, String>()
        map[ApiUrls.AccountNumber] = AccountNumber
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        map["Type"] = type
        map["HourlyType"] = "H"
        map["Mode"] = mode
        map["Year"] = "0"
        AppLog.printLog("getWaterUsageRequestModel: " + Gson().toJson(map))

        return map;
    }

    fun getBillingDetailsRequestModel(
        AccountNumber: String,
        data: ResponseModelClasses.DataBaseUtils
    ): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.AccountNumber, AccountNumber)
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)

        AppLog.printLog("getBillingDetailsRequestModel: " + Gson().toJson(map))

        return map;
    }

    //Connect With Utility
    fun getConnectWithUtilityRequestModel(data: ResponseModelClasses.DataBaseUtils): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)

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
        cPassword: String, data: ResponseModelClasses.DataBaseUtils
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
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        AppLog.printLog("getSignupRequestModel: " + Gson().toJson(map))
        return map;
    }

    fun getUpdateAccountRequestModel(
        email: String,
        HomePhone: String,
        MobilePhone: String,
        BusinessHomePhone: String,
        BillingPreference: String,
        Custid: String,
        AccountNumber: String,
        editAnswer1: String,
        editAnswer2: String,
        sQuesID1: String,
        sQuesID2: String, data: ResponseModelClasses.DataBaseUtils
    ): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.EmailID, email)
        map.put(ApiUrls.HomePhone, HomePhone)
        map.put(ApiUrls.MobilePhone, MobilePhone)
        map.put(ApiUrls.BusinessHomePhone, BusinessHomePhone)
        map.put(ApiUrls.BillingPreference, BillingPreference)
        map.put(ApiUrls.Custid, Custid)
        map.put(ApiUrls.AccountNumber, AccountNumber)
        map.put(ApiUrls.SecurityQuestionId, sQuesID1)
        map.put(ApiUrls.SecurityQuestionId2, sQuesID2)
        map.put(ApiUrls.HintsAns, editAnswer1)
        map.put(ApiUrls.HintsAns2, editAnswer2)
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        AppLog.printLog("getUpdateAccountRequestModel: " + Gson().toJson(map))
        return map;
    }

    fun getForgetRequestStepOne(
        username: String,
        data: ResponseModelClasses.DataBaseUtils
    ): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.UserName, username);
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        AppLog.printLog("getForgetRequestStepOne: " + Gson().toJson(map))
        return map;
    }

    fun getForgetRequestStepTwo(
        username: String,
        editAnswer1RP: String,
        editAnswer2RP: String, data: ResponseModelClasses.DataBaseUtils
    ): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.UserName, username);
        map.put(
            ApiUrls.SecurityQuestion1,
            ResetPassSecurityQuestionData.getArrayItem(0).SecurityQuestionId
        );
        map.put(
            ApiUrls.SecurityQuestion2,
            ResetPassSecurityQuestionData.getArrayItem(1).SecurityQuestionId
        );
        map.put(ApiUrls.Answer1, editAnswer1RP)
        map.put(ApiUrls.Answer2, editAnswer2RP)
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        AppLog.printLog("getForgetRequestStepTwo: " + Gson().toJson(map))
        return map;
    }

    fun getForgetRequestStepThree(
        username: String,
        newPass: String,
        cNewPass: String, data: ResponseModelClasses.DataBaseUtils
    ): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.UserName, username);
        map.put(ApiUrls.Password, newPass);
        map.put(ApiUrls.ConfirmPassword, cNewPass)
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        AppLog.printLog("getForgetRequestStepThree: " + Gson().toJson(map))
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
        MeterNumber: String, data: ResponseModelClasses.DataBaseUtils
    ): Map<String, String> {
        var map = HashMap<String, String>()

        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        map.put(
            "MobileNumber",
            TrackleApp.appContext?.let { AppPrefences.getProfileInfo(it).MobilePhone }.toString()
        )
        map.put("PostalCode", PostalCode)
        map.put(
            "CustomerId",
            TrackleApp.appContext?.let { AppPrefences.getProfileInfo(it).CustomerId }.toString()
        )
        map.put(
            "EmailId",
            TrackleApp.appContext?.let { AppPrefences.getProfileInfo(it).EmailId }.toString()
        )
        map.put(ApiUrls.UtilityAccountNumber, UtilityAccountNumber)
        map.put("UtilityId", "0")
        map.put(ApiUrls.MeterNumber, MeterNumber)

        AppLog.printLog("getAddAccountRequestModel: " + Gson().toJson(map))
        return map;
    }


    /*["AccountNumber": "3", "DataSource": "aquatraxserver.database.windows.net", "Database": "AQUATRAXDEV", "DBPassword": "newaqt@123", "DBUserName": "aquatrax"]*/
    //Get Usage Notification
    fun getUsageNotificationRequestModel(
        AccountNumber: String,
        data: ResponseModelClasses.DataBaseUtils
    ): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.AccountNumber, AccountNumber)
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)

        AppLog.printLog("getUsageNotificationRequestModel: " + Gson().toJson(map))

        return map;
    }


    /* ["DBUserName": "aquatrax", "DBPassword": "newaqt@123", "MonthlyThreshold": "123",
     "DailyThreshold": "23", "MeterNumber": "09526591", "AccountNumber": "3", "Unit": "CCF",
      "Database": "AQUATRAXDEV", "DataSource": "aquatraxserver.database.windows.net"]*/
    //Set Usage Notification
    fun setUsageNotificationRequestModel(
        MonthlyThreshold: String,
        DailyThreshold: String,
        MeterNumber: String,
        AccountNumber: String,
        Unit: String, data: ResponseModelClasses.DataBaseUtils
    ): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)

        map.put("MonthlyThreshold", MonthlyThreshold)
        map.put("DailyThreshold", DailyThreshold)
        map.put("MeterNumber", MeterNumber)
        map.put("AccountNumber", AccountNumber)
        map.put("Unit", Unit)

        AppLog.printLog("setUsageNotificationRequestModel: " + Gson().toJson(map))

        return map;
    }

    //Connect Me Request Model
    /*"emailid": "utkarsh3441@gmail.com", "TopicID": "3", "Subject": "test", "Body": "Message for  wwwccw",
     "AccountNumber": "1234567", "Database": "AQUATRAXDEV", "DBPassword": "newaqt@123",
     "DataSource": "aquatraxserver.database.windows.net", "DBUserName": "aquatrax"]*/
    fun getConnectMeRequestModel(
        emailid: String,
        TopicID: String,
        Subject: String,
        Body: String,
        AccountNumber: String, data: ResponseModelClasses.DataBaseUtils
    ): HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put(ApiUrls.EmailID, emailid)
        map.put(ApiUrls.TopicID, TopicID)
        map.put(ApiUrls.Subject, Subject)
        map.put(ApiUrls.Body, Body)
        map.put(ApiUrls.AccountNumber, AccountNumber)

        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        AppLog.printLog("getConnectMeRequestModel: " + Gson().toJson(map))
        return map;
    }

    fun getDeleteAccountRequestModel(
        AccountNumber: String,
        data: ResponseModelClasses.DataBaseUtils
    ): HashMap<String, String> {
        var map = HashMap<String, String>()

        map.put(ApiUrls.DataSource, data.ServerName)
        map.put(ApiUrls.Database, data.DataBaseName)
        map.put(ApiUrls.DBUserName, data.UserName)
        map.put(ApiUrls.DBPassword, data.Password)
        AppLog.printLog("getDeleteAccountRequestModel: " + Gson().toJson(map))
        return map;
    }

}