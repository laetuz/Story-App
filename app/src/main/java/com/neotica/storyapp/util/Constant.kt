package com.neotica.storyapp.util

import android.Manifest
import androidx.datastore.preferences.core.stringPreferencesKey

object Constant {
    const val KEY_STORY = "story"

    val KEY_USERID = stringPreferencesKey("userId")
    val KEY_NAME = stringPreferencesKey("name")
    val KEY_TOKEN = stringPreferencesKey("name")

    const val TAG = "Response:::"
/*-------------*/
    const val DATA = "DATA"
    const val CAMERA_X_RESULT = 200
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    const val REQUEST_CODE_PERMISSIONS = 10

    const val SUCCESS = "success"
    const val ERROR = "error"
}