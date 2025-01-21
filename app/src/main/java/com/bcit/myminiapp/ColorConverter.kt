package com.bcit.myminiapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bcit.myminiapp.data.ColorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ConvertColorDisplay(navController: NavController, colorRepository: ColorRepository) {
    // State for the input value
    val hexValue = remember { mutableStateOf("") }
    val rgbValue = remember { mutableStateListOf(mutableStateOf(""), mutableStateOf(""), mutableStateOf("")) }
    val hslValue = remember { mutableStateListOf(mutableStateOf(""), mutableStateOf(""), mutableStateOf("")) }

    // Conversion states
    val convertFromState = remember { mutableStateOf("HEX") }
    val convertToState = remember { mutableStateOf("HEX") }
    val convertedResult = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        // Convert From options
        item { ConvertOptions("Convert From", convertFromState) }

        // Convert To options
        item { ConvertOptions("Convert To", convertToState) }

        // Text Fields
        item {
            InsertTextField(
                convertFromState.value,
                hexValue,
                rgbValue[0], rgbValue[1],rgbValue[2],
                hslValue[0], hslValue[1], hslValue[2]
            )
        }

        // The Convert Button
        item {
            ConvertButton(
                convertFromState,
                convertToState,
                hexValue,
                rgbValue,
                hslValue,
                convertedResult,
                colorRepository,
                coroutineScope
            )
        }

        // Result
        item {
            if (convertedResult.value.isNotEmpty()) {
                ConversionResult(convertedResult.value)
            }
        }
    }
}

// Convert to and from options
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertOptions(label: String, convertState: MutableState<String>) {
    val options = listOf("HEX", "RGB", "HSL")
    var selectedIndex by remember { mutableStateOf(0) }

    Text(label, fontSize = 25.sp, modifier = Modifier.fillMaxWidth())

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
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

// The Convert Button
@Composable
fun ConvertButton(
    convertFromState: MutableState<String>,
    convertToState: MutableState<String>,
    hexValue: MutableState<String>,
    rgbValue: SnapshotStateList<MutableState<String>>,
    hslValue: SnapshotStateList<MutableState<String>>,
    convertedResult: MutableState<String>,
    colorRepository: ColorRepository,
    coroutineScope: CoroutineScope
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
                    val input = getInputValue(convertFromState.value, hexValue, rgbValue, hslValue)
                    if (input.isBlank()) {
                        convertedResult.value = "Please fill in all fields before converting."
                        return@launch
                    }
                    val result = performConversion(input, convertFromState.value, convertToState.value, colorRepository)
                    convertedResult.value = result
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D3541))
        ) {
            Text(
                "Convert",
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

// The Conversion itself
suspend fun performConversion(
    input: String,
    from: String,
    to: String,
    repository: ColorRepository
): String {
    return try {
        // Check if the source and target formats are the same
        if (to.lowercase() == from.lowercase()) {
            return "Conversion unnecessary"
        }

        // Perform the conversion
        val result = repository.convertColor(from.lowercase(), to.lowercase(), input)
        when (to.lowercase()) {
            "hex" -> result?.hex ?: "Conversion failed."
            "rgb" -> result?.rgb ?: "Conversion failed."
            "hsl" -> result?.hsl ?: "Conversion failed."
            else -> "Invalid conversion type."
        }
    } catch (e: Exception) {
        "Error occurred: ${e.message}"
    }
}


// The Conversion Result
@Composable
fun ConversionResult(result: String) {
    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Text(
            "Converted Result",
            fontSize = 25.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Card {
            Text(
                result,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
        }
    }
}
