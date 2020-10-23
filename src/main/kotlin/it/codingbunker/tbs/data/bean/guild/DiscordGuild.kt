package it.codingbunker.tbs.data.bean.guild

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.locations.*
import it.codingbunker.tbs.utils.Costant.BASE_API_URL
import org.apache.commons.text.StringEscapeUtils
import org.litote.kmongo.Data

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Location("$BASE_API_URL/discord/guild/")
class DiscordGuild constructor() {

    /*
       TODO serverId set Empty to avoid
       com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException: Instantiation of
       value failed for JSON property serverId due to missing (therefore NULL) value for creator parameter serverId which is a non-nullable type
     */
    constructor(
        @JsonProperty("guildId", required = true)
        guildId: String = "",
        @JsonProperty("symbolCommand")
        symbolCommand: String,
        @JsonProperty("guildName", required = true)
        guildName: String
    ) : this() {
        this.guildId = guildId
        this.symbolCommand = symbolCommand
        this.guildName = guildName
    }

    @Location("/{serverId}")
    class DiscordGuildGet(val parent: DiscordGuild, var serverId: String)

    lateinit var guildId: String

    var symbolCommand: String = "%"
        get() = StringEscapeUtils.unescapeJava(field)
        set(value) {
            field = StringEscapeUtils.escapeJava(value)
        }

    lateinit var guildName: String
}