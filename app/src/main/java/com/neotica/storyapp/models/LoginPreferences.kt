package com.neotica.storyapp.models

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.neotica.storyapp.response.user.UserModel
import com.neotica.storyapp.util.Constant.KEY_NAME
import com.neotica.storyapp.util.Constant.KEY_TOKEN
import com.neotica.storyapp.util.Constant.KEY_USERID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LoginPreferences (context: Context) {
    private val preference = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

    fun setToken(token:String){
        val edit = preference.edit()
        edit.putString(TOKEN,token)
        edit.apply()
    }

/*    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[TOKEN] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }*/

    fun getToken(): String? {
        return preference.getString(TOKEN, null)
    }

    fun clearToken(){
        val edit = preference.edit().clear()
        edit.apply()
    }

    companion object{
        const val PREF_NAME = "login_pref"
        const val TOKEN = "token"
    }
}