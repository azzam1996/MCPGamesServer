package data.api

import data.api.dto.GameDto
import domain.model.Game
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ClientApi(private val client: HttpClient) : Api {

    override suspend fun getGames(): List<GameDto?>? {
        return client.get {
            url("https://www.freetogame.com/api/games")
        }.body()

    }

    override suspend fun getGamesByCategory(category: String): List<GameDto?>? {
        return client.get {
            url("https://www.freetogame.com/api/games?category=${category}")
        }.body()
    }
}