package it.codingbunker.tbs.feature.managment.repository

import com.github.kittinunf.result.coroutines.SuspendableResult
import it.codingbunker.tbs.common.client.discord.model.DiscordOAuth2User
import it.codingbunker.tbs.feature.managment.table.*
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

interface UserRepository {

    suspend fun existUserByDiscordId(discordId: String): Boolean

    suspend fun generateUserByDiscordUser(discordOAuth2User: DiscordOAuth2User): UserDTO

    suspend fun findUserByDiscordId(discordId: String): SuspendableResult<UserDTO, Exception>
}

class UserRepositoryImpl(private val authorizedAdminUserList: List<String>) : UserRepository {
    override suspend fun existUserByDiscordId(discordId: String): Boolean {
        return newSuspendedTransaction {
            User.find {
                Users.discordId eq discordId
            }.empty().not()
        }
    }

    override suspend fun generateUserByDiscordUser(discordOAuth2User: DiscordOAuth2User) =
        newSuspendedTransaction {
            User.new {
                discordId = discordOAuth2User.id
                username = discordOAuth2User.username
                discriminator = discordOAuth2User.discriminator
                email = discordOAuth2User.email
                mfaEnabled = discordOAuth2User.mfaEnabled
                avatarHash = discordOAuth2User.avatar
                userRoles = if (authorizedAdminUserList.find { it == discordOAuth2User.id } != null) {
                    SizedCollection(Role[RoleType.ADMIN])
                } else {
                    SizedCollection(Role[RoleType.USER])
                }
            }.convertToUserDTO()
        }

    override suspend fun findUserByDiscordId(discordId: String): SuspendableResult<UserDTO, Exception> =
        newSuspendedTransaction {
            SuspendableResult.of {
                val result = User.find {
                    Users.discordId eq discordId
                }

                if (result.empty()) {
                    throw NoSuchElementException()
                } else {
                    result.first().convertToUserDTO()
                }
            }
        }

    private fun User.convertToUserDTO() =
        UserDTO(
            id = id.toString(),
            discordId = discordId,
            username = username,
            discriminator = discriminator,
            email = email,
            mfaEnabled = mfaEnabled,
            userRoles = userRoles.map {
                it.id.value
            }.toSet(),
            avatarHash = avatarHash,
            avatarImageData = avatarImageData?.bytes,
            dateCreation = dateCreation
        )


}