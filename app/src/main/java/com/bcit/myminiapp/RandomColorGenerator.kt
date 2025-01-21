package com.bcit.myminiapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bcit.myminiapp.data.ColorRepository
import com.bcit.myminiapp.data.GeneratedColor
import com.bcit.myminiapp.ui.state.ColorState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun GetFiveColors(navController: NavController, colorRepository: ColorRepository) {
    val colorState = ColorState(colorRepository)
    val colorCodeState = remember { mutableStateOf("HEX") }
    val pinnedColors = remember { mutableStateListOf<GeneratedColor>() } // Tracks pinned colors
    val coroutineScope = rememberCoroutineScope() // For launching coroutines

    LaunchedEffect(colorState) {
        colorState.getColors() // Initial color generation
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Color Code Options
        item() {
            GenerateCodeOptions("Display Code", colorCodeState)
        }

        // Display Random Colors
        items(colorState.colors.size) { index ->
            val fontColor = remember(colorState.colors[index]) {
                calculateFontColor(colorState.colors[index].hex)
            }
            val color = colorState.colors[index]
            when (colorCodeState.value.lowercase()) {
                "hex" -> InsertColorCard(color, "hex", fontColor, pinnedColors)
                "rgb" -> InsertColorCard(color, "rgb", fontColor, pinnedColors)
                "hsl" -> InsertColorCard(color, "hsl", fontColor, pinnedColors)
            }
        }

        // Generate Button
        item {
            InsertRegenerateButton(coroutineScope, colorRepository, colorState, pinnedColors)
        }
    }
}

// The regenerate button
@Composable
fun InsertRegenerateButton(
    coroutineScope: CoroutineScope, colorRepository: ColorRepository,
    colorState: ColorState, pinnedColors: MutableList<GeneratedColor>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    colorRepository.regenerateColors(colorState, pinnedColors, colorRepository)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2D3541)
            )
        ) {
            Text(
                text = "Regenerate",
                fontSize = 22.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

// Convert to and from options
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateCodeOptions(label: String, convertState: MutableState<String>) {
    val options = listOf("HEX", "RGB", "HSL")
    var selectedIndex by remember { mutableStateOf(0) }

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 10.dp)
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    selectedIndex = index
                    convertState.value = label
                },
                selected = index == selectedIndex,
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Text(label)
            }
        }
    }
}