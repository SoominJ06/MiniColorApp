package com.bcit.myminiapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Insert Text Fields
@Composable
fun InsertTextField(
    option: String?, hexValue: MutableState<String>,
    rValue: MutableState<String>, gValue: MutableState<String>, bValue: MutableState<String>,
    hValue: MutableState<String>, sValue: MutableState<String>, lValue: MutableState<String>
) {
    when (option) {
        "HEX" -> HexTextField(hexValue)
        "RGB" -> RGBTextField(rValue, gValue, bValue)
        "HSL" -> HSLTextField(hValue, sValue, lValue) // Replace with actual HSL state
        else -> Text("Invalid Option Selected")
    }
}

// Customized Text Field
@Composable
fun MyTextField(label: String, valueToChange: MutableState<String>) {
    TextField(
        value = valueToChange.value,
        modifier = Modifier
            .fillMaxWidth(),
        onValueChange = { valueToChange.value = it },
        label = { Text(label) },
        textStyle = TextStyle(fontSize = 20.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

// Text Field for HEX code
@Composable
fun HexTextField(hexValue: MutableState<String>) {
    Text("Enter HEX Value", fontSize = 25.sp, modifier = Modifier.padding(vertical = 20.dp))
    MyTextField("Hex Number", hexValue)
}

// Text Fields for RGB code
@Composable
fun RGBTextField(
    rValue: MutableState<String>,
    gValue: MutableState<String>,
    bValue: MutableState<String>
) {
    Text("Enter RGB Value", fontSize = 25.sp, modifier = Modifier.padding(vertical = 20.dp))
    MyTextField("R (0-255)", rValue)
    MyTextField("G (0-255)", gValue)
    MyTextField("B (0-255)", bValue)
}

// Text Fields for HSL code
@Composable
fun HSLTextField(
    hValue: MutableState<String>,
    sValue: MutableState<String>,
    lValue: MutableState<String>
) {
    Text("Enter HSL Value", fontSize = 25.sp, modifier = Modifier.padding(vertical = 20.dp))
    MyTextField("H (0º-360º)", hValue)
    MyTextField("S (0%-100%)", sValue)
    MyTextField("L (0%-100%)", lValue)
}

// Function to get input value from text field
fun getInputValue(
    from: String,
    hexValue: MutableState<String>,
    rgbValue: SnapshotStateList<MutableState<String>>,
    hslValue: SnapshotStateList<MutableState<String>>
): String {
    return when (from.uppercase()) {
        "HEX" -> hexValue.value.trim()
        "RGB" -> rgbValue.joinToString("-") { it.value.trim() }
        "HSL" -> hslValue.joinToString("-") { it.value.trim() }
        else -> ""
    }
}