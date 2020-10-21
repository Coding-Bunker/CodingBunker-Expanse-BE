package it.codingbunker.tbs.data.bean.guild

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.locations.*
import it.codingbunker.tbs.utils.Costant.BASE_API_URL
import org.apache.commons.text.StringEscapeUtils
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Data
import org.litote.kmongo.id.StringId

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Location("$BASE_API_URL/discord/guild/")
open class DiscordGuild constructor() {

    /*
       TODO serverId set Empty to avoid
       com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException: Instantiation of
       value failed for JSON property serverId due to missing (therefore NULL) value for creator parameter serverId which is a non-nullable type
     */
    constructor(
        @JsonProperty("serverId", required = true)
        serverId: String = "",
        @JsonProperty("symbolCommand")
        symbolCommand: String,
        @JsonProperty("guildName", required = true)
        guildName: String
    ) : this() {
        this.guildId = StringId(serverId)
        this.symbolCommand = symbolCommand
        this.guildName = guildName
    }

    @Location("/{serverId}")
    class DiscordGuildGet(val parent: DiscordGuild, var serverId: String)

    @BsonId
    lateinit var guildId: StringId<String>

    var symbolCommand: String = "%"
        get() = StringEscapeUtils.unescapeJava(field)
        set(value) {
            field = StringEscapeUtils.escapeJava(value)
        }

    lateinit var guildName: String
}