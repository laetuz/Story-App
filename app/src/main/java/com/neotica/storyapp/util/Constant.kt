package com.neotica.storyapp.util

import android.Manifest

object Constant {
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    const val REQUEST_CODE_PERMISSIONS = 10
    val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    const val ACCESS_PERMISSION_DEFAULT = 101
}