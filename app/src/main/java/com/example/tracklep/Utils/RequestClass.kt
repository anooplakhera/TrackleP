package com.example.tracklep.Utils

import com.example.tracklep.ApiClient.ApiUrls
import com.example.tracklep.DataClasses.ResetPassSecurityQuestionData
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
        map.put(ApiUrls.HintAns, editAnswer1)
        map.put(ApiUrls.HintsAns2, editAnswer2)
        map.put(ApiUrls.DataSource, ApiUrls.DataSource_value)
        map.put(ApiUrls.Database, ApiUrls.Database_value)
        map.put(ApiUrls.DBUserName, ApiUrls.DBUserName_value)
        map.put(ApiUrls.DBPassword, ApiUrls.DBPassword_value)
        AppLog.printLog("getSignupRequestModel: " + Gson().toJson(map))
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
}