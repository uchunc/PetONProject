package com.woo.peton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.maps.MapsInitializer
import com.woo.peton.core.ui.theme.PetONProjectTheme
import com.woo.peton.ui.screen.EntryPointScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, null)

        enableEdgeToEdge()
        setContent {
            PetONProjectTheme {
                EntryPointScreen()
            }
        }
    }
}