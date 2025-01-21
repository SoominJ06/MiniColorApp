package com.bcit.myminiapp.ui.state

import androidx.compose.runtime.mutableStateListOf
import com.bcit.myminiapp.data.ColorRepository
import com.bcit.myminiapp.data.GeneratedColor

class ColorState(private val colorRepository: ColorRepository) {

    var colors = mutableStateListOf<GeneratedColor>()

    suspend fun getColors() {
        colors.also {
            it.clear()
            it.addAll(colorRepository.getColors(5))
        }
    }
}