package com.bcit.myminiapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bcit.myminiapp.data.ColorRepository

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomePageDisplay(navController: NavController, colorRepository: ColorRepository) {
    // Remember a mutable state for the random color
    var randomColor by remember { mutableStateOf("#292830") } // Default color

    // Use LaunchedEffect to fetch the random color when the composable is first launched
    LaunchedEffect(Unit) {
        randomColor = colorRepository.getSingleColor().hex
    }

    LazyColumn(verticalArrangement = Arrangement.Center) {
        item {
            Text("Mini Color App", fontSize = 45.sp, textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#292830")),
                modifier = Modifier.padding(20.dp).fillMaxWidth())
        }
        item {
            InsertLogo(randomColor)
        }
        item {
            GenerateButton("Generate Random Colors", navController, "generateColors")
        }
        item {
            GenerateButton("Convert Color Codes", navController, "convertCode")
        }
    }
}

@Composable
fun GenerateButton(label: String, navController: NavController, navigateTo: String) {
    // Display button
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier.padding(top = 25.dp),
            onClick = {
                navController.navigate(navigateTo)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2D3541)
            )
        ) {
            Text(
                label, fontSize = 25.sp, textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#e9e9e9")),
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
            )
        }
    }
}

@Composable
fun InsertLogo(randomColor: String) {
    // Calculating whether the icon should be black or white
    val fontColor = remember(randomColor) {
        calculateFontColor(randomColor)
    }
    val icon = if (fontColor.equals(android.graphics.Color.parseColor("#e9e9e9"))) {
        R.drawable.colordrop_black
    } else {
        R.drawable.colordrop_white
    }

    // Insert Logo
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp) // Size of the circle
                .clip(CircleShape)
                .background(Color(android.graphics.Color.parseColor(randomColor))),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(90.dp) // Size of the icon
            )
        }
    }
}