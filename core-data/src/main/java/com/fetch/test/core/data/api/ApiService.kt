package com.fetch.test.core.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Creates and configures an HttpClient for Ktor.
 * Includes JSON content negotiation using Kotlinx Serialization.
 */
fun createHttpClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
}

/**
 * Ktor-based service for fetching items from the API.
 */
class ApiService(private val httpClient: HttpClient) {
    private val baseUrl = "https://hiring.fetch.com"

    /**
     * Fetches a list of items from the API.
     * @return A list of Item objects.
     * @throws Exception if the network request fails.
     */
    suspend fun getItems(): List<com.fetch.test.domain.model.ListItem> {
        return httpClient.get("${baseUrl}/hiring.json").body()
    }
}
