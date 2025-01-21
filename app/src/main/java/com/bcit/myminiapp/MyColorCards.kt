package com.bcit.myminiapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bcit.myminiapp.data.GeneratedColor

@Composable
fun InsertColorCard(color: GeneratedColor, code: String, fontColor: Color, pinnedColors: MutableList<GeneratedColor>) {

    // Getting the color in different codes
    val colorCode = when (code) {
        "rgb" -> color.rgb
        "hsl" -> color.hsl
        else -> color.hex
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(android.graphics.Color.parseColor(color.hex)))
            .padding(20.dp, vertical = 38.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = colorCode,
            fontSize = 28.sp,
            color = fontColor
        )

        InsertIcon(color, fontColor, pinnedColors)
    }
}

@Composable
fun InsertIcon(color: GeneratedColor, fontColor: Color, pinnedColors: MutableList<GeneratedColor>) {
    IconButton(
        onClick = {
            // Toggle pin/unpin
            if (pinnedColors.contains(color)) {
                pinnedColors.remove(color) // Unpin
            } else {
                pinnedColors.add(color) // Pin
            }
        }
    ) {
        Icon(
            imageVector = if (pinnedColors.contains(color)) {
                Icons.Filled.Favorite
            } else {
                Icons.Outlined.FavoriteBorder
            },
            tint = fontColor, // Yellow for pinned
            contentDescription = "Pin",
            modifier = Modifier.size(40.dp)
        )
    }
}