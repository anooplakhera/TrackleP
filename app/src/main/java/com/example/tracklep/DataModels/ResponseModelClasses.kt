package com.example.tracklep.DataModels

object ResponseModelClasses {
    data class SecurityQuestionResponse(
        val Parameters: String,
        val Question: String,
        val SecurityQuestionId: String,
        val SqlQuery: String
    )

    data class ResetPassStep1Response(
        val Table: ArrayList<TableData>
    ) {
        data class TableData(
            val Status: String,
            val Message: String,
            val SecurityQuestionId: String,
            val Question: String
        )
    }

    data class ResetPassStep2Response(
        val Table: ArrayList<TableData1>
    ) {
        data class TableData1(
            val STATUS: String,
            val Message: String,
            val AttemptLeft: String
        )
    }

    /*-------Registration-------*/
    data class RegistrationResponse(
        val Status: String,
        val Message: String,
        val CustomerId: String,
        val IsMailSucceed: String,
        val SqlQuery: String,
        val Parameters: String
    )

    //Login Response
    data class LoginResponseModel(
        val expires: String,
        val issued: String,
        val AccountNumber: String,
        val CompanyWebsite: String,
        val CustomerId: String,
        val CustomerTypeDesc: String,
        val Email: String,
        val FacebookUrl: String,
        val IsAMI: String,
        val MeterType: String,
        val Name: String,
        val PrivacyPolicy: String,
        val TwitterURL: String,
        val UtilityAccountNumber: String,
        val UtilityLogo: String,
        val access_token: String,
        val expires_in: Int,
        val token_type: String
    )

    //Get Utility List - Registration
    data class UtilityListResponseModel(val Query: Query1, val Results: Results1) {
        data class Query1(val Parameters: Any, val SqlQuery: Any)
        data class Results1(val Table1: ArrayList<Table>) {
            data class Table(
                val AdminUserCount: Int,
                val CreatedBy: Int,
                val CreatedDate: String,
                val DataBaseName: String,
                val LastUpdated: String,
                val Name: String,
                val Password: String,
                val PrimaryPhone: String,
                val SecondryPhone: String,
                val ServerName: String,
                val Status: Int,
                val UpdatedBy: Int,
                val UserCount: Int,
                val UserName: String,
                val UtilityId: Int,
                val Website: String
            )
        }
    }

    //Billing Dashboard
    data class BillingDashboardResponse(
        val Parameters: String,
        val Question: String,
        val SecurityQuestionId: String,
        val SqlQuery: String
    )

}



