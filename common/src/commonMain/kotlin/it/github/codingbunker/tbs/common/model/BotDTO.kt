package it.github.codingbunker.tbs.common.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BotDTO(
    @SerialName("id")
    var id: String,
    @SerialName("botName")
    var botName: String,
    @SerialName("botKey")
    var botKey: String,
    @SerialName("botDateCreation")
    var botDateCreation: Instant,
    @SerialName("botRoles")
    var botRoles: Set<RoleType>
)