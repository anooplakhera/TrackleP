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
}


