package domain.repository

import domain.model.Game
import domain.model.GameDetails
import domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface GamesRepository {
    suspend fun getGames(): Resource<List<Game?>?>
    suspend fun getGamesByCategory(category: String): Resource<List<String?>?>
}