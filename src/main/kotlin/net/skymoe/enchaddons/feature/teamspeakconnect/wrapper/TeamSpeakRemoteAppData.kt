package net.skymoe.enchaddons.feature.teamspeakconnect.wrapper

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val type: String,
    val payload: AuthRequestPayload,
)

@Serializable
data class AuthRequestPayload(
    val identifier: String,
    val version: String,
    val name: String,
    val description: String,
    val content: AuthRequestContent,
)

@Serializable
data class AuthRequestContent(
    val apiKey: String,
)

@Serializable
data class AuthResponse(
    val type: String,
    val payload: AuthResponsePayload,
    val status: Status,
)

@Serializable
data class AuthResponsePayload(
    val apiKey: String,
    // Add other fields you expect in the auth response
)

@Serializable
data class Status(
    val code: Int,
    val message: String,
)

@Serializable
data class KeyPressRequest(
    val type: String,
    val payload: KeyPressPayload,
)

@Serializable
data class KeyPressPayload(
    val button: String,
    val state: Boolean,
)

// Events here

@Serializable
data class ConnectStatusChangedEvent(
    val type: String = "connectStatusChanged",
    val payload: ConnectStatusChangedPayload,
)

/*
    status: 0 -> Disconnect, 1 -> Connecting, 2 -> Connected, 3 -> Joined Server, 4 -> Full loaded
 */
@Serializable
data class ConnectStatusChangedPayload(
    val connectionId: Int,
    val error: Int,
    val hotReload: Boolean,
    val info: ConnectStatusInfo? = null,
    val status: Int,
)

/*
    serverName, serverUid just available when status == 2
 */
@Serializable
data class ConnectStatusInfo(
    var clientId: Int? = null,
    val serverName: String? = null,
    val serverUid: String? = null,
)

@Serializable
data class ServerPropertiesUpdatedEvent(
    val type: String = "serverPropertiesUpdated",
    val payload: ServerPropertiesUpdatedPayload,
)

@Serializable
data class ServerPropertiesUpdatedPayload(
    val connectionId: Int,
    val properties: ServerProperties,
)

@Serializable
data class ServerProperties(
    val address: String,
    val antiFloodPointsNeededCommandBlock: Int,
    val antiFloodPointsNeededIpBlock: Int,
    val antiFloodPointsNeededPluginBlock: Int,
    val antiFloodPointsTickReduce: Int,
    val askForPrivilegeKey: Boolean,
    val autostart: Boolean,
    val canonicalName: String,
    val capabilityExtensions: String,
    val channelTempDeleteDelayDefault: Int,
    val channelsOnline: Int,
    val clientConnections: Int,
    val clientsOnline: Int,
    val codecEncryptionMode: Int,
    val complainAutobanCount: Int,
    val complainAutobanTime: Int,
    val complainRemoveTime: Int,
    val created: Long,
    val defaultChannelAdminGroup: String,
    val defaultChannelGroup: String,
    val defaultServerGroup: String,
    val downloadQuota: Long,
    val flagPassword: Boolean,
    val homebaseStorageQuota: Long,
    val hostBannerGfxInterval: Int,
    val hostBannerGfxUrl: String,
    val hostBannerMode: Int,
    val hostBannerUrl: String,
    val hostButtonGfxUrl: String,
    val hostButtonTooltip: String,
    val hostButtonUrl: String,
    val hostMessage: String,
    val hostMessageMode: Int,
    val iconId: Int,
    val id: String,
    val ip: String,
    val logChannel: Boolean,
    val logClient: Boolean,
    val logFiletransfer: Boolean,
    val logPermissions: Boolean,
    val logQuery: Boolean,
    val logServer: Boolean,
    val machineId: String,
    val maxClients: Int,
    val maxDownloadTotalBandwidth: String,
    val maxHomebases: Int,
    val maxUploadTotalBandwidth: String,
    val minAndroidVersion: Int,
    val minClientVersion: Int,
    val minClientsInChannelBeforeForcedSilence: Int,
    val minIosVersion: Int,
    val minWinphoneVersion: Int,
    val monthBytesDownloaded: Int,
    val monthBytesUploaded: Int,
    val mytsidConnectOnly: Boolean,
    val name: String,
    val namePhonetic: String,
    val neededIdentitySecurityLevel: Int,
    val nickname: String,
    val platform: String,
    val port: Int,
    val prioritySpeakerDimmModificator: Double,
    val queryClientConnections: Int,
    val queryClientsOnline: Int,
    val reservedSlots: Int,
    val storageQuota: Long,
    val totalBytesDownloaded: String,
    val totalBytesUploaded: String,
    val totalPacketLossControl: Double,
    val totalPacketLossKeepAlive: Double,
    val totalPacketLossSpeech: Double,
    val totalPacketLossTotal: Double,
    val uniqueIdentifier: String,
    val uploadQuota: Long,
    val uptime: Int,
    val uuid: String,
    val version: String,
    val webListEnabled: Boolean,
    val welcomeMessage: String,
)

@Serializable
data class ChannelsEvent(
    val type: String = "channels",
    val payload: ChannelsPayload,
)

@Serializable
data class ChannelsPayload(
    val connectionId: Int,
    val hotReload: Boolean,
    val info: ChannelsInfo,
)

@Serializable
data class ChannelsInfo(
    val rootChannels: List<RootChannel>,
    val subChannels: Map<String, List<SubChannel>>, // Key is parent channel ID
)

@Serializable
data class RootChannel(
    val id: String,
    val order: String,
    val parentId: String,
    val properties: ChannelProperties,
)

@Serializable
data class SubChannel(
    val id: String,
    val order: String,
    val parentId: String,
    val properties: ChannelProperties,
)

@Serializable
data class ChannelProperties(
    val bannerGfxUrl: String,
    val bannerMode: Int,
    val codec: Int,
    val codecIsUnencrypted: Boolean,
    val codecLatencyFactor: Int,
    val codecQuality: Int,
    val deleteDelay: Int,
    val description: String,
    val flagAreSubscribed: Boolean,
    val flagDefault: Boolean,
    val flagMaxclientsUnlimited: Boolean,
    val flagMaxfamilyclientsInherited: Boolean,
    val flagMaxfamilyclientsUnlimited: Boolean,
    val flagPassword: Boolean,
    val flagPermanent: Boolean,
    val flagSemiPermanent: Boolean,
    val forcedSilence: Boolean,
    val iconId: Int,
    val maxclients: Int,
    val maxfamilyclients: Int,
    val name: String,
    val namePhonetic: String,
    val neededTalkPower: Int,
    val order: String,
    val permissionHints: Int,
    val storageQuota: Int,
    val topic: String,
    val uniqueIdentifier: String,
)

@Serializable
data class ChannelEditedEvent(
    val type: String = "channelEdited",
    val payload: ChannelEditedPayload,
)

@Serializable
data class ChannelEditedPayload(
    val channelId: String,
    val connectionId: Int,
    val invoker: InvokerInfo,
    val properties: ChannelProperties,
)

@Serializable
data class ChannelCreatedEvent(
    val type: String = "channelCreated",
    val payload: ChannelCreatedPayload,
)

@Serializable
data class ChannelCreatedPayload(
    val channelId: String,
    val connectionId: Int,
    val invoker: InvokerInfo,
    val parentId: String,
    val properties: ChannelProperties,
)

@Serializable
data class InvokerInfo(
    val id: Int,
    val nickname: String,
    val uid: String,
)

@Serializable
data class ClientMovedEvent(
    val type: String = "clientMoved",
    val payload: ClientMovedPayload,
)

@Serializable
data class ClientMovedPayload(
    val clientId: Int,
    val connectionId: Int,
    val hotReload: Boolean,
    val newChannelId: String,
    val oldChannelId: String,
    val properties: ClientProperties? = null,
    val type: Int,
    val visibility: Int,
)

@Serializable
data class ClientPropertiesUpdatedEvent(
    val type: String = "clientPropertiesUpdated",
    val payload: ClientPropertiesUpdatedPayload,
)

@Serializable
data class ClientPropertiesUpdatedPayload(
    val clientId: Int,
    val connectionId: Int,
    val properties: ClientProperties,
)

@Serializable
data class ClientProperties(
    val away: Boolean,
    val awayMessage: String,
    val badges: String,
    val channelGroupId: String,
    val channelGroupInheritedChannelId: String,
    val country: String,
    val created: Int,
    val databaseId: String,
    val defaultChannel: String,
    val defaultChannelPassword: String,
    val defaultToken: String,
    val description: String,
    val flagAvatar: String,
    val flagTalking: Boolean,
    val iconId: Int,
    val idleTime: Int,
    val inputDeactivated: Boolean,
    val inputHardware: Boolean,
    val inputMuted: Boolean,
    val integrations: String,
    val isChannelCommander: Boolean,
    val isMuted: Boolean,
    val isPrioritySpeaker: Boolean,
    val isRecording: Boolean,
    val isStreaming: Boolean,
    val isTalker: Boolean,
    val lastConnected: Int,
    val metaData: String,
    val monthBytesDownloaded: Int,
    val monthBytesUploaded: Int,
    val myteamspeakAvatar: String,
    val myteamspeakId: String,
    val neededServerQueryViewPower: Int,
    val nickname: String,
    val nicknamePhonetic: String,
    val outputHardware: Boolean,
    val outputMuted: Boolean,
    val outputOnlyMuted: Boolean,
    val permissionHints: Int,
    val platform: String,
    val serverGroups: String,
    val serverPassword: String,
    val signedBadges: String,
    val talkPower: Int,
    val talkRequest: Int,
    val talkRequestMsg: String,
    val totalBytesDownloaded: Int,
    val totalBytesUploaded: Int,
    val totalConnections: Int,
    val type: Int,
    val uniqueIdentifier: String,
    val unreadMessages: Int,
    val userTag: String,
    val version: String,
    val volumeModificator: Double,
)

@Serializable
data class TalkStatusChangedEvent(
    val type: String = "talkStatusChanged",
    val payload: TalkStatusChangedPayload,
)

@Serializable
data class TalkStatusChangedPayload(
    val clientId: Int,
    val connectionId: Int,
    val isWhisper: Boolean,
    val status: Int,
)
