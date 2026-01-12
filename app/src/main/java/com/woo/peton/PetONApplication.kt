package com.woo.peton

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetONApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PetON = this
    }
    companion object{
        private lateinit var PetON : PetONApplication
    }
}