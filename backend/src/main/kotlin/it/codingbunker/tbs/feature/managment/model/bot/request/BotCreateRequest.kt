package it.codingbunker.tbs.feature.managment.model.bot.request

import it.github.codingbunker.tbs.common.model.RoleType
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BotCreateRequest(
    @SerialName("botName") @Required var botName: String,
    @SerialName("roleList") @Required var roleList: Set<RoleType>
)
