package com.mE.Health.utility

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.mE.Health.models.UserDataResponse
import javax.inject.Inject

class AppSession @Inject constructor(
    private var sharedPref: SharedPreferences
) {
    private var userDTO: UserDataResponse? = null
    private var prefsEditor: SharedPreferences.Editor? = null

    companion object {
        private const val PREF_AUTHENTICATED = "authState"
    }

    init {
        try {
            prefsEditor = sharedPref.edit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setUserData(authenticated: UserDataResponse?){
        val gson = Gson()
        val json = gson.toJson(authenticated)
        prefsEditor?.putString(PREF_AUTHENTICATED, json)
        prefsEditor?.commit()
    }

    fun getUserData(): UserDataResponse? {
        val gson = Gson()
        val json = sharedPref?.getString(PREF_AUTHENTICATED, null) ?: return null
        return gson.fromJson(json, UserDataResponse::class.java)
    }

    var token: String
        get() = sharedPref.getString("Token ", "")!!
        set(token) {
            sharedPref.edit { putString("Token ", "Token $token") }
        }
}