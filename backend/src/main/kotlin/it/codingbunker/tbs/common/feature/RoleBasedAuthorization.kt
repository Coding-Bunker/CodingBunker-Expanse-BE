package it.codingbunker.tbs.common.feature

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import it.codingbunker.tbs.feature.managment.table.RoleType
import org.slf4j.LoggerFactory

class AuthorizationException(override val message: String) : Exception(message)

class RoleBasedAuthorization(config: Configuration) {

    private val logger = LoggerFactory.getLogger(RoleBasedAuthorization::class.java)
    private val getRoles = config._getRoles

    class Configuration {
        internal var _getRoles: (Principal) -> Set<RoleType> = { emptySet() }

        fun getRoles(gr: (Principal) -> Set<RoleType>) {
            _getRoles = gr
        }
    }

    fun interceptPipeline(
        pipeline: ApplicationCallPipeline,
        anyRoleSet: Set<RoleType>? = null,
        allRoleSet: Set<RoleType>? = null,
        noneRoleSet: Set<RoleType>? = null
    ) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Features, Authentication.ChallengePhase)
        pipeline.insertPhaseAfter(Authentication.ChallengePhase, AuthorizationPhase)

        pipeline.intercept(AuthorizationPhase) {
            val principal =
                call.authentication.principal<Principal>() ?: throw AuthorizationException("Missing principal")
            val roles = getRoles(principal)
            val denyReasons = mutableListOf<String>()
            allRoleSet?.let {
                val missing = allRoleSet - roles
                if (missing.isNotEmpty()) {
                    denyReasons += "Principal $principal lacks required role(s) ${missing.joinToString(" and ")}"
                }
            }
            anyRoleSet?.let {
                if (anyRoleSet.none { it in roles }) {
                    denyReasons += "Principal $principal has none of the sufficient role(s) ${
                    anyRoleSet.joinToString(
                        " or "
                    )
                    }"
                }
            }
            noneRoleSet?.let {
                if (noneRoleSet.any { it in roles }) {
                    denyReasons += "Principal $principal has forbidden role(s) ${
                    (noneRoleSet.intersect(roles)).joinToString(" and ")
                    }"
                }
            }
            if (denyReasons.isNotEmpty()) {
                val message = denyReasons.joinToString(". ")
                logger.warn("Authorization failed for ${call.request.path()}. $message")
                throw AuthorizationException(message)
            }
        }
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, RoleBasedAuthorization> {
        override val key = AttributeKey<RoleBasedAuthorization>("RoleBasedAuthorization")

        val AuthorizationPhase = PipelinePhase("Authorization")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): RoleBasedAuthorization {
            val configuration = Configuration().apply(configure)
            return RoleBasedAuthorization(configuration)
        }
    }
}

class AuthorizedRouteSelector(private val description: String) :
    RouteSelector(RouteSelectorEvaluation.qualityConstant) {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

    override fun toString(): String = "(authorize $description)"
}

fun Route.withRole(role: RoleType, build: Route.() -> Unit) = authorizedRoute(all = setOf(role), build = build)

fun Route.withAllRoles(vararg roles: RoleType, build: Route.() -> Unit) =
    authorizedRoute(all = roles.toSet(), build = build)

fun Route.withAnyRole(vararg roles: RoleType, build: Route.() -> Unit) =
    authorizedRoute(any = roles.toSet(), build = build)

fun Route.withoutRoles(vararg roles: RoleType, build: Route.() -> Unit) =
    authorizedRoute(none = roles.toSet(), build = build)

private fun Route.authorizedRoute(
    any: Set<RoleType>? = null,
    all: Set<RoleType>? = null,
    none: Set<RoleType>? = null,
    build: Route.() -> Unit
): Route {

    val description = listOfNotNull(
        any?.let { "anyOf (${any.joinToString(" ")})" },
        all?.let { "allOf (${all.joinToString(" ")})" },
        none?.let { "noneOf (${none.joinToString(" ")})" }
    ).joinToString(",")
    val authorizedRoute = createChild(AuthorizedRouteSelector(description))
    application.feature(RoleBasedAuthorization).interceptPipeline(authorizedRoute, any, all, none)
    authorizedRoute.build()
    return authorizedRoute
}
