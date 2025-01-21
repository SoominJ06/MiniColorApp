package com.bcit.myminiapp

import android.app.Application
import com.bcit.myminiapp.data.ColorRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

class MyApp : Application() {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    val colorRepository by lazy {
        ColorRepository(client)
    }

}