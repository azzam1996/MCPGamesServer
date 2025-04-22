package data.repository

import data.api.Api
import data.mappers.toGame

import domain.model.Game
import domain.repository.GamesRepository
import domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GamesRepositoryImpl(private val api: Api) : GamesRepository {
    override suspend fun getGames(): Resource<List<Game?>?> {
        // emit(Resource.Loading())
        try {
            val gamesFromApi = api.getGames()
            val games = gamesFromApi?.map { gameDto ->
                gameDto?.let {
                    it.toGame()
                }
            }
            return (Resource.Success(games))
        } catch (e: Exception) {
            return (Resource.Error(message = e.message ?: "Unknown Error"))
        }
    }

    override suspend fun getGamesByCategory(category: String): Resource<List<String?>?> {
        //emit(Resource.Loading())
        try {
            val gamesFromApi = api.getGamesByCategory(category)
            val games = gamesFromApi?.map { gameDto ->
                gameDto?.let {
                    it.title
                }
            }
            return (Resource.Success(games))
        } catch (e: Exception) {
            return (Resource.Error(message = e.message ?: "Unknown Error"))
        }
    }
}