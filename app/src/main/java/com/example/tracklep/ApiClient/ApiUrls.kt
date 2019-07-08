package com.example.tracklep.ApiClient

object ApiUrls {

    /*-----------------Api+Urls----------------*/
    const val BASE_URL: String = "https://aquatraxapi-dev.azurewebsites.net/"
//    const val BASE_URL: String = "https://aquatraxapi.azurewebsites.net/"
    const val BASE_PATH: String = "api/customer/"
    const val LOGIN = "token";
    const val REGISTER_USER = "register"
    const val FORGET = "reset-password/";
    const val STEP_ONE = "step-one";
    const val STEP_TWO = "step-two";
    const val SECURITY_QUESTION = "security-questions"
    const val Authorization = "authorization";
    const val Account = "account";
    const val UpdateAccount = "update-account";


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
    const val PostalCode = "PostalCode"
    const val UserId = "UserId"
    const val MeterNumber = "MeterNumber"
    const val SecurityQuestionId = "SecurityQuestionId"
    const val SecurityQuestionId2 = "SecurityQuestionId2"
    const val HintAns = "HintAns"
    const val HintsAns2 = "HintsAns2"
    const val GrantType = "grant_type"
    const val TanentId = "tanentid"


    fun getBasePathUrl(): String {
        return BASE_URL + BASE_PATH
    }

}