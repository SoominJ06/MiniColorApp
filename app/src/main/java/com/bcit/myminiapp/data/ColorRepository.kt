package com.bcit.myminiapp.data

import com.bcit.myminiapp.ui.state.ColorState
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ColorRepository(private val client: HttpClient) {

    suspend fun getColors(number: Int): List<GeneratedColor> {
        val response = client.get(Endpoints.GENERATE_COLORS.url.format(number))
        val json = response.body<JsonArray>().toString() // Parse directly as a JsonArray
        return Gson().fromJson(json, Array<GeneratedColor>::class.java).toList()
    }

    suspend fun getSingleColor(): GeneratedColor {
        val response = client.get(Endpoints.GENERATE_RANDOM_COLOR.url)
        val json = response.body<JsonObject>().toString()
        return Gson().fromJson(json, GeneratedColor::class.java)
    }

    suspend fun regenerateColors(
        colorState: ColorState,
        pinnedColors: List<GeneratedColor>,
        colorRepository: ColorRepository
    ) {
        // Number of colors to regenerate
        val neededColors = 5 - pinnedColors.size

        // Fetch new colors only if more are needed
        val newColor = if (neededColors <= 1) { // Only add one color to list
            if (neededColors == 0) {
                return // No new colors to return
            }
            colorRepository.getSingleColor() // Grab a single color
        } else { // Add more than one color
            // Grab the new colors
            val newColors = colorRepository.getColors(neededColors)

            // Combine pinned colors with the newly fetched colors
            colorState.colors.clear()
            colorState.colors.addAll(pinnedColors)
            colorState.colors.addAll(newColors)
            return
        }

        // Combine pinned colors with the newly fetched color
        colorState.colors.clear()
        colorState.colors.addAll(pinnedColors)
        colorState.colors.add(newColor)
    }

    suspend fun convertColor(convertFrom: String, convertTo: String, code: String): GeneratedColor {
        val response = client.get(Endpoints.CONVERT_CODE.url.format(convertFrom, convertTo, code))
        val json = response.body<JsonObject>().toString()
        return Gson().fromJson(json, GeneratedColor::class.java)
    }
}