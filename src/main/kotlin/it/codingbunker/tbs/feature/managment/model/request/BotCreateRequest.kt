package it.codingbunker.tbs.feature.managment.model.request

import com.fasterxml.jackson.annotation.JsonProperty
import it.codingbunker.tbs.data.table.RoleType

class BotCreateRequest(
	@JsonProperty("botName", required = true)
	var botName: String,
	@JsonProperty("roleList", required = true)
	var roleList: Set<RoleType>
)