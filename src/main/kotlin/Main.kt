package com.GamesServer

import data.api.ClientApi
import data.remote.KtorClient
import data.repository.GamesRepositoryImpl
import org.koin.core.context.startKoin

import domain.repository.GamesRepository
import domain.util.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.java.KoinJavaComponent.inject

fun main() = `run mcp server`()