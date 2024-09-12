package com.example.app.utils

import android.content.Context
import androidx.core.content.edit

object Share {
    private const val PREFS_NAME = "users"

    fun storeData(
        name: String,
        email: String,
        bio: String,
        username: String,
        imageUrl: String,
        context: Context
    ) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putString("name", name)
            putString("email", email)
            putString("bio", bio)
            putString("username", username)
            putString("imageUrl", imageUrl)
        }
    }

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getUserName(context: Context): String =
        getSharedPreferences(context).getString("username", "") ?: ""

    fun getImageUrl(context: Context): String =
        getSharedPreferences(context).getString("imageUrl", "") ?: ""

    fun getEmail(context: Context): String =
        getSharedPreferences(context).getString("email", "") ?: ""

    fun getName(context: Context): String =
        getSharedPreferences(context).getString("name", "") ?: ""

    fun getBio(context: Context): String =
        getSharedPreferences(context).getString("bio", "") ?: ""
}