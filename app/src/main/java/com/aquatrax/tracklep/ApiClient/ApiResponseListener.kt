package com.aquatrax.tracklep.ApiClient

import retrofit2.Call

/**
 * Created
 * by heena on 4/1/18.
 */

interface ApiResponseListener {
    /**
     * Call on success API response with request code
     */
    fun onApiResponse(call: Call<Any>, response: Any)

    /*Call on error of API with request code */
    fun onApiError(call: Call<Any>, throwable: Throwable)
}