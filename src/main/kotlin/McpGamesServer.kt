package com.GamesServer

import data.api.ClientApi
import data.remote.KtorClient
import data.repository.GamesRepositoryImpl
import domain.util.Resource
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.streams.*
import io.modelcontextprotocol.kotlin.sdk.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.io.asSink
import kotlinx.io.buffered
import kotlinx.serialization.json.*

// Main function to run the MCP server
fun `run mcp server`() {

    val repository = GamesRepositoryImpl(ClientApi(KtorClient.createClient()))

    // Create the MCP Server instance with a basic implementation
    val server = Server(
        Implementation(
            name = "Games", // Tool name is "Games"
            version = "1.0.0" // Version of the implementation
        ),
        ServerOptions(
            capabilities = ServerCapabilities(tools = ServerCapabilities.Tools(listChanged = true))
        )
    )

    // Register a tool to fetch weather alerts by state
    server.addTool(
        name = "get_games_by_category",
        description = """
            Get Games by Category. Input is Category as Text and the Output is List of Games ex. shooter
        """.trimIndent(),
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("category") {
                    put("type", "string")
                    put("description", "Category for the games that you want")
                }
            },
            required = listOf("category")
        )
    ) { request ->
        val category = request.arguments["category"]?.jsonPrimitive?.content
        if (category == null) {
            return@addTool CallToolResult(
                content = listOf(TextContent("The 'category' parameter is required."))
            )
        }

        val result = repository.getGamesByCategory(category)
        when (result) {
            is Resource.Error<*> -> {
                CallToolResult(error(result.message ?: ""))
            }

            is Resource.Success<*> -> {
                CallToolResult(content = (result.data ?: emptyList()).map { TextContent(it) })
            }
        }
    }


    // Create a transport using standard IO for server communication
    val transport = StdioServerTransport(
        System.`in`.asInput(),
        System.out.asSink().buffered()
    )

    runBlocking {
        server.connect(transport)
        val done = Job()
        server.onClose {
            done.complete()
        }
        done.join()
    }
}
