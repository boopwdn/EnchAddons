package net.skymoe.enchaddons.feature.teamspeakconnect.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.skymoe.enchaddons.getLogger
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

val logger = getLogger("TeamSpeak Wrapper")

class TeamSpeakRemoteAppWrapper(
    private val websocketUrl: String,
    private val identifier: String,
    private val version: String,
    private val name: String,
    private val description: String,
    private val apiKey: String, // Store API key for subsequent connections
    private val coroutineScope: CoroutineScope,
) {
    private val okHttpClient: OkHttpClient get() =
        OkHttpClient
            .Builder()
            .pingInterval(10, TimeUnit.SECONDS) // Optional: Keep-alive
            .build()
    private var webSocket: WebSocket? = null
    private val json =
        Json {
            ignoreUnknownKeys = true
        } // Configure Json for deserialization
    private var connectionJob: Job? = null

    var onConnect: (() -> Unit)? = null
    var onDisconnect: (() -> Unit)? = null
    var onAuthSuccess: ((AuthResponsePayload) -> Unit)? = null
    var onMessageReceived: ((String) -> Unit)? = null // Generic message handler
    var onError: ((Throwable) -> Unit)? = null

    // Event specific handlers.  Add handlers for each event type your application needs.
    var onConnectStatusChanged: ((ConnectStatusChangedPayload) -> Unit)? = null
    var onServerPropertiesUpdated: ((ServerPropertiesUpdatedPayload) -> Unit)? = null
    var onChannels: ((ChannelsPayload) -> Unit)? = null
    var onChannelCreated: ((ChannelCreatedPayload) -> Unit)? = null
    var onChannelEdited: ((ChannelEditedPayload) -> Unit)? = null
    var onClientMoved: ((ClientMovedPayload) -> Unit)? = null
    var onClientPropertiesUpdated: ((ClientPropertiesUpdatedPayload) -> Unit)? = null
    var onTalkStatusChanged: ((TalkStatusChangedPayload) -> Unit)? = null

    fun connect() {
        if (connectionJob?.isActive == true) {
            logger.warn("Already connected or connecting, ignoring...")
            return
        }

        connectionJob?.cancel()
        connectionJob =
            coroutineScope.launch {
                try {
                    val request = Request.Builder().url(websocketUrl).build()

                    val listener =
                        object : WebSocketListener() {
                            override fun onOpen(
                                webSocket: WebSocket,
                                response: Response,
                            ) {
                                this@TeamSpeakRemoteAppWrapper.webSocket = webSocket
                                logger.info("WebSocket connection opened")
                                coroutineScope.launch { onConnect?.invoke() } // Use coroutineScope
                                authenticate()
                            }

                            override fun onMessage(
                                webSocket: WebSocket,
                                text: String,
                            ) {
//                                println("Received message: $text")
                                coroutineScope.launch { onMessageReceived?.invoke(text) } // Use coroutineScope
                                handleMessage(text)
                            }

                            override fun onMessage(
                                webSocket: WebSocket,
                                bytes: ByteString,
                            ) {
                            }

                            override fun onClosing(
                                webSocket: WebSocket,
                                code: Int,
                                reason: String,
                            ) {
                                this@TeamSpeakRemoteAppWrapper.webSocket = null
                                connectionJob?.cancel() // Connection ended
                                coroutineScope.launch { onDisconnect?.invoke() } // Use coroutineScope
                            }

                            override fun onClosed(
                                webSocket: WebSocket,
                                code: Int,
                                reason: String,
                            ) {
                                logger.info("WebSocket closed: $code, $reason")
                                this@TeamSpeakRemoteAppWrapper.webSocket = null
                                connectionJob?.cancel() // Connection ended
                                coroutineScope.launch { onDisconnect?.invoke() } // Use coroutineScope
                            }

                            override fun onFailure(
                                webSocket: WebSocket,
                                t: Throwable,
                                response: Response?,
                            ) {
                                coroutineScope.launch { onError?.invoke(t) } // Use coroutineScope
                                this@TeamSpeakRemoteAppWrapper.webSocket = null
                                connectionJob?.cancel() // Connection failed
                            }
                        }

                    webSocket = okHttpClient.newWebSocket(request, listener)
                } catch (e: Exception) {
                    logger.info("Error during connection: ${e.message}")
                    coroutineScope.launch { onError?.invoke(e) } // Use coroutineScope
                    connectionJob?.cancel()
                }
            }
    }

    fun disconnect() {
        webSocket?.close(1000, "Normal closure")
        okHttpClient.dispatcher.executorService.shutdown() // Stop OkHttp Client
        connectionJob?.cancel() // Cancel connection Job
    }

    private fun authenticate() {
        val authRequest =
            AuthRequest(
                type = "auth",
                payload =
                    AuthRequestPayload(
                        identifier = identifier,
                        version = version,
                        name = name,
                        description = description,
                        content = AuthRequestContent(apiKey), // Use existing API key, or empty string
                    ),
            )
        val jsonString = json.encodeToString(authRequest)
        webSocket?.send(jsonString)
    }

    private fun handleMessage(message: String) {
        try {
            // First, parse the message to get the "type" field.
            val jsonElement = json.parseToJsonElement(message).jsonObject
            val type = jsonElement["type"]?.jsonPrimitive?.contentOrNull

            when (type) {
                "auth" -> {
                    val authResponse = json.decodeFromString<AuthResponse>(message)
                    if (authResponse.status.code == 0) {
                        onAuthSuccess?.invoke(authResponse.payload)
                    } else {
                        logger.error("Authentication failed: ${authResponse.status.message}")
                        onError?.invoke(Exception("Authentication failed: ${authResponse.status.message}"))
                    }
                }
                "connectStatusChanged" -> {
                    val event = json.decodeFromString<ConnectStatusChangedEvent>(message)
                    onConnectStatusChanged?.invoke(event.payload)
                }
                "serverPropertiesUpdate" -> {
                    val event = json.decodeFromString<ServerPropertiesUpdatedEvent>(message)
                    onServerPropertiesUpdated?.invoke(event.payload)
                }
                "channels" -> {
                    val event = json.decodeFromString<ChannelsEvent>(message)
                    onChannels?.invoke(event.payload)
                }
                "channelCreated" -> {
                    val event = json.decodeFromString<ChannelCreatedEvent>(message)
                    onChannelCreated?.invoke(event.payload)
                }
                "channelEdited" -> {
                    val event = json.decodeFromString<ChannelEditedEvent>(message)
                    onChannelEdited?.invoke(event.payload)
                }
                "clientMoved" -> {
                    val event = json.decodeFromString<ClientMovedEvent>(message)
                    onClientMoved?.invoke(event.payload)
                }
                "clientPropertiesUpdated" -> {
                    val event = json.decodeFromString<ClientPropertiesUpdatedEvent>(message)
                    onClientPropertiesUpdated?.invoke(event.payload)
                }
                "talkStatusChanged" -> {
                    val event = json.decodeFromString<TalkStatusChangedEvent>(message)
                    onTalkStatusChanged?.invoke(event.payload)
                }
                // Handle other event types here.
                else -> {
//                    println("Unhandled message type: $type")
                }
            }
        } catch (e: SerializationException) {
//            println("Serialization exception: ${e.message}")
            onError?.invoke(e)
        } catch (e: Exception) {
//            println("Error handling message: ${e.message}")
            onError?.invoke(e)
        }
    }

    fun sendKeyPress(
        button: String,
        state: Boolean,
    ) {
        val keyPressRequest =
            KeyPressRequest(
                type = "keyPress",
                payload =
                    KeyPressPayload(
                        button = button,
                        state = state,
                    ),
            )
        val jsonString = json.encodeToString(keyPressRequest)
        webSocket?.send(jsonString)
    }
}
