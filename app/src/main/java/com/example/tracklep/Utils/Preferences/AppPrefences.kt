package com.example.hp.togelresultapp.Preferences

import android.content.Context
import com.example.tracklep.DataModels.ResponseModelClasses
import com.google.gson.Gson


object AppPrefences {


    private val PREFS_FILE_NAME = "Trackle"
    private val DeviceId = "deviceId"
    private val USERID = "userid"
    private val WakeUpStatus = "wakeup"
    private val LanguageSelect = "language"
    private val TapPosition = "pos"
    private val CompetitorId = "CompetitorId"
    private val DateWiseData = "date_wise"
    private val TokenID = "tokenId"
    private val Login = "login"
    private val LoginModel = "LoginModel"


    fun clearAll(ctx: Context) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )

        prefs.edit().clear().commit()
    }

    fun getLogin(ctx: Context): Boolean? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getBoolean(Login, false)
    }

    fun setLogin(ctx: Context, data: Boolean) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean(Login, data)
        editor.commit()
    }


    fun getUserid(ctx: Context): String? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(USERID, "")
    }

    fun setUserId(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(USERID, data)
        editor.commit()
    }

    fun getLoginModel(ctx: Context): ResponseModelClasses.LoginResponseModel? {
        var data = ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).getString(LoginModel, "")
        val gson = Gson().fromJson(data, ResponseModelClasses.LoginResponseModel::class.java)
        return gson
    }

    fun setLoginModel(ctx: Context, data: ResponseModelClasses.LoginResponseModel) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(LoginModel, data.toString())
        editor.commit()
    }


    fun getDeviceId(ctx: Context): String {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(DeviceId, "").toString()
    }


    fun setDeviceId(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(DeviceId, data)
        editor.commit()
    }

    fun getlanguage(ctx: Context): String? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(LanguageSelect, "")
    }

    fun setlanguage(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(LanguageSelect, data)
        editor.commit()
    }

    fun getTokenID(ctx: Context): String? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(TokenID, "")
    }

    fun setTokenID(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(TokenID, data)
        editor.commit()
    }

    fun getPosition(ctx: Context): String? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(TapPosition, "")
    }

    fun setPosition(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(TapPosition, data)
        editor.commit()
    }

    fun getDateWiseData(ctx: Context): String? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(DateWiseData, "")
    }

    fun setDateWiseData(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(DateWiseData, data)
        editor.commit()
    }

    fun getCompetitorId(ctx: Context): String? {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .getString(CompetitorId, "")
    }

    fun setCompetitorId(ctx: Context, data: String) {
        val prefs = ctx.getSharedPreferences(
            PREFS_FILE_NAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(CompetitorId, data)
        editor.commit()
    }


    fun offKeepScreen(c: Context) {
        val prefs = c.getSharedPreferences(
            PREFS_FILE_NAME,
            Context.MODE_PRIVATE
        )
        prefs.edit().putBoolean(WakeUpStatus, false).commit()
    }

    fun setWakeup(c: Context) {
        val prefs = c.getSharedPreferences(
            PREFS_FILE_NAME,
            Context.MODE_PRIVATE
        )
        prefs.edit().putBoolean(WakeUpStatus, true).commit()
    }

    fun isKeepOn(c: Context): Boolean {
        val prefs = c.getSharedPreferences(
            PREFS_FILE_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean(WakeUpStatus, false)
    }


}