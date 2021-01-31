package it.codingbunker.tbs.data.dto

import it.codingbunker.tbs.data.table.RoleType

class BotJWTDTO(
	override val id: String,
	val botName: String,
	override val role: Set<RoleType>,
	override val profileType: ProfileDTO.ProfileType
) : ProfileDTO