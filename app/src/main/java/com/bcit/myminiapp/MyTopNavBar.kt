package com.bcit.myminiapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopNav(navController: NavController, currDestination: String) {

    // Update title of nav bar
    val label = if (currDestination.contains("generate")) {
        "Color Generator"
    } else if (currDestination.contains("convert")) {
        "Color Code Converter"
    } else {
        "Mini Color App"
    }

    TopAppBar(
        title = {
            Text(label, textAlign = TextAlign.Center, fontSize = 23.sp, modifier = Modifier.fillMaxWidth())
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null,
                    modifier = Modifier.size(30.dp))
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate("home")
            }) {
                Icon(Icons.Default.Home, contentDescription = null,
                    modifier = Modifier.size(30.dp))
            }
        }
    )
}