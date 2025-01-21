package com.bcit.myminiapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bcit.myminiapp.data.ColorRepository

/**
 * Soomin Jeong
 * A01374778
 * Set 3G
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colorRepository = (application as MyApp).colorRepository

        setContent {
            GetMainContent(colorRepository)
        }
    }
}

// Grabbing Nav bar and Displaying content depending on which page it is
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GetMainContent(colorRepository: ColorRepository) {
    val navController = rememberNavController()
    val currDestination = remember { mutableStateOf("home") }

    // Observe the current back stack entry
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currDestination.value = destination.route ?: "home"
        }
    }

    Scaffold(
        topBar = {
            MyTopNav(navController, currDestination.value)
        }
    ) { padding ->
        NavHost(
            navController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(padding)
        ) {
            composable("home") {
                HomePageDisplay(navController, colorRepository)
            }

            composable("generateColors") {
                GetFiveColors(navController, colorRepository)
            }

            composable("convertCode") {
                ConvertColorDisplay(navController, colorRepository)
            }
        }
    }
}

// Helper function to calculate the font color based on background color
fun calculateFontColor(hexColor: String): Color {
    val color = android.graphics.Color.parseColor(hexColor)
    val r = android.graphics.Color.red(color)
    val g = android.graphics.Color.green(color)
    val b = android.graphics.Color.blue(color)

    // Calculate luminance
    val luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b

    // Return black or white based on luminance
    return if (luminance > 128) {
        Color(android.graphics.Color.parseColor("#292830"))
    } else {
        Color(android.graphics.Color.parseColor("#e9e9e9"))
    }
}