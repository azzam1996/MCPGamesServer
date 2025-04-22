package data.api

import data.api.dto.GameDto


interface Api {
    suspend fun getGames(): List<GameDto?>?
    suspend fun getGamesByCategory(category: String): List<GameDto?>?
}