package com.graphql4k

import com.expediagroup.graphql.TopLevelNames
import com.expediagroup.graphql.TopLevelObject
import com.expediagroup.graphql.execution.SimpleKotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.federation.FederatedSchemaGeneratorConfig
import com.expediagroup.graphql.federation.FederatedSchemaGeneratorHooks
import com.expediagroup.graphql.federation.execution.FederatedTypeRegistry
import com.expediagroup.graphql.federation.toFederatedSchema
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.GraphQL
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind

private val defaultJacksonObjectMapper = jacksonObjectMapper()
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)

class GraphQL4k(
    private val supportedPackages: List<String>,
    private val queries: Set<Query>,
    private val mutations: Set<Mutation>,
    private val mapper: ObjectMapper = defaultJacksonObjectMapper,
    private val endpoint: String = "/api/graphql"
) {

    fun toHttpHandler(): RoutingHttpHandler {

        val schema = toFederatedSchema(
            config = FederatedSchemaGeneratorConfig(
                supportedPackages = supportedPackages,
                dataFetcherFactoryProvider = SimpleKotlinDataFetcherFactoryProvider(),
                topLevelNames = TopLevelNames(),
                hooks = FederatedSchemaGeneratorHooks(FederatedTypeRegistry())
            ),
            queries = queries.map { TopLevelObject(it) },
            mutations = mutations.map { TopLevelObject(it) }
        )

        val graphql = GraphQL.newGraphQL(schema)
            .build()

        return endpoint bind Method.POST to { request: Request ->
                val executionResult = graphql
                    .execute(request.bodyString())
                when {
                    executionResult.isDataPresent -> {
                        val data = executionResult.getData<Any>()
                        val response = GraphQLResponse(data = data, errors = null)
                        Response(OK).body(response.toJson())
                    }
                    executionResult.errors.isNotEmpty() -> {
                        val response = GraphQLResponse(data = null, errors = executionResult.errors)
                        Response(BAD_REQUEST).body(response.toJson())
                    }
                    else -> Response(OK)
                }
            }
    }

    private fun GraphQLResponse<*>.toJson(): String = mapper.writeValueAsString(this)
}

