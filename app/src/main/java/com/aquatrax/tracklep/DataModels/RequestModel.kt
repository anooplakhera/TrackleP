package com.aquatrax.tracklep.DataModels

class RequestModel {

    data class Registration(val deviceId: String)
    data class ResetPasswordStep1(val UserName: String)


}