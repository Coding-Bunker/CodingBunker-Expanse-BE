package it.codingbunker.tbs.data.repo

import it.codingbunker.tbs.data.bean.guild.DiscordGuild
import it.codingbunker.tbs.data.client.TakaoMongoClient
import org.litote.kmongo.Id
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection


interface DiscordRepositoryInterface {
    fun createDiscordGuild(discordGuild: DiscordGuild)

    fun existDiscordGuildById(guildId: Id<String>): Boolean
}

class DiscordRepository(private val takaoMongoClient: TakaoMongoClient) : DiscordRepositoryInterface {


    override fun createDiscordGuild(discordGuild: DiscordGuild) {
        takaoMongoClient.mongoClientDB.getCollection<DiscordGuild>().run {
            if (existDiscordGuildById(discordGuild.guildId)) {
                return
            }
            insertOne(discordGuild)
        }
    }

    override fun existDiscordGuildById(guildId: Id<String>): Boolean {
        return takaoMongoClient.mongoClientDB.getCollection<DiscordGuild>().run {
            findOne { DiscordGuild::guildId eq guildId } != null
        }
    }
}