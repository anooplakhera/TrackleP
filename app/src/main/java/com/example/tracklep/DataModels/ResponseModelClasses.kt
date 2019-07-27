package com.example.tracklep.DataModels

object ResponseModelClasses {
    data class SecurityQuestionResponse(
        val Parameters: String,
        val Question: String,
        val SecurityQuestionId: String,
        val SqlQuery: String,
        var isSelected: Boolean=false
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
        data class Results1(val Table: ArrayList<Table1>) {
            data class Table1(
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



    data class MyProfileResponse(
        val Parameters: String,
        val Question: String,
        val SecurityQuestionId: String,
        val SqlQuery: String
    )

    //MeterDetails Resposne

    data class MeterDetails(
        val Query: Query1,
        val Results: Results1
    ) {
        data class Results1(
            val Table: List<TableOne>,
            val Table1: List<TableTwo>
        ) {
            data class TableTwo(
                val IsNonAMI: Boolean,
                val ShowDecimal: Boolean
            )

            data class TableOne(
                val IsAMI: Boolean,
                val MeterNumber: String,
                val MeterType: String,
                val Status: Int
            )
        }

        data class Query1(
            val Parameters: Any,
            val SqlQuery: Any
        )
    }

    //WaterUsages
    data class WaterUsages(
        val Query: Query1,
        val Results: Results1
    ) {
        data class Results1(
            val Table: List<TableOne>

        ) { data class TableOne(
                val Month: Int,
                val Year: String,
                val TotalValue: String,
                val ZipCode: String,
                val AVERAGE: String,
                val HIGHEST: String,
                val AllocationValue: String,
                val HighUsage: String,
                val UsageDate: String,
                val LOWEST: String
            )
        }

        data class Query1(
            val Parameters: Any,
            val SqlQuery: Any
        )
    }


    //My Profile
    data class MyProfile(
        val AccountNumber: Int,
        val AddressId: Int,
        val AlternateEmailID: Any,
        val CityName: String,
        val CommunicationAddress: String,
        val CommunicationAddress1: String,
        val CommunicationAddress2: Any,
        val CommunicationZipCode: String,
        val CustomerId: Int,
        val CustomerNo: String,
        val DefaultAccountNumber: Int,
        val DefaultAddressId: Int,
        val DefaultPayId: Any,
        val DefaultPayType: Any,
        val EmailId: String,
        val FullName: String,
        val HintsAns: String,
        val HintsAns2: String,
        val HomePhone: String,
        val IsPOBox: Any,
        val MobilePhone: String,
        val Parameters: Any,
        val Properties: String,
        val SecurityQuestion: String,
        val SecurityQuestion2: Any,
        val SecurityQuestionId: Int,
        val SecurityQuestionId2: Int,
        val SqlQuery: Any,
        val StateName: String,
        val UtilityAccountNumber: String
    )


    //Billing Dashboard

    data class BillingDashboardResponse(
        val Query: Query1,
        val Results: Results1
    ) {
        data class Results1(
            val Table: List<TableOne>,
            val Table1: List<TableTwo>
        ) {
            data class TableTwo(
                val CustomerId: String,
                val UtilityAccountNumber: String,
                val AccountBalance: String,
                val HeaderName: String
            )

            data class TableOne(
                val HeadId: Int,
                val Section: String,
                val HeaderName: String,
                val SortOrder: Int,
                val HeaderType: Int,
                val ColorCode: String,
                val Value: Any,
                val ImagePath: Any,
                val ImageToolTipTextKey: Any,
                val ImageLinkPath: Any,
                val IsShow: Int,
                val BillingId: Any,
                val LastRechargeAmount: Int
            )
        }

        data class Query1(
            val Parameters: Any,
            val SqlQuery: Any
        )
    }

    //My Profile
    data class UpdateProfile(
        val Status: String,
        val Message: String,
        val CustomerId: String,
        val IsMailSucceed: Boolean,
        val SqlQuery: Any,
        val Parameters: Any
    )


    //My Profile
    data class BarChart(
        val count: Int,
        val range: Float
    )


}



