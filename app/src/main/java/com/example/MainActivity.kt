package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MainScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.VedvoraTheme
import com.example.viewmodel.VedvoraViewModel

sealed class AppScreen {
    object Welcome : AppScreen()
    object MainPortal : AppScreen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VedvoraTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: VedvoraViewModel = viewModel()
                    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Welcome) }

                    Crossfade(
                        targetState = currentScreen,
                        label = "ScreenTransition"
                    ) { screen ->
                        when (screen) {
                            AppScreen.Welcome -> {
                                WelcomeScreen(
                                    onEnterPortalWithDetails = { name, building, flat ->
                                        viewModel.updateResidentDetails(name, building, flat)
                                        viewModel.showToast("Welcome $name! Credentials saved.")
                                        currentScreen = AppScreen.MainPortal
                                    },
                                    onLoginClick = {
                                        viewModel.showToast("Welcome back! Auto-authenticated.")
                                        currentScreen = AppScreen.MainPortal
                                    }
                                )
                            }
                            AppScreen.MainPortal -> {
                                MainScreen(
                                    viewModel = viewModel,
                                    onSignOut = { currentScreen = AppScreen.Welcome }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

