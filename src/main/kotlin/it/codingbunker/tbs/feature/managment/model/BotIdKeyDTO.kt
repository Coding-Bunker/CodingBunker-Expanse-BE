package it.codingbunker.tbs.feature.managment.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BotIdKeyDTO(
    @SerialName("botId") @Required var botId: String,
    @SerialName("botKey") @Required var botKey: String
)
