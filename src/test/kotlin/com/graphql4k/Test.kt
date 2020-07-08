package com.graphql4k

import com.graphql4k.api.PersonMutation
import com.graphql4k.api.PersonQuery
import com.graphql4k.api.ProjectMutation
import com.graphql4k.api.ProjectQuery
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.kodein.di.*

fun main() {

    val kodein = DI {
        bind() from setBinding<Query>()
        bind() from setBinding<Mutation>()
        bind<Query>().inSet() with singleton { ProjectQuery() }
        bind<Query>().inSet() with singleton { PersonQuery() }
        bind<Mutation>().inSet() with singleton { ProjectMutation() }
        bind<Mutation>().inSet() with singleton { PersonMutation() }
    }

    GraphQL4k(
        supportedPackages = listOf("com.graphql4k.api"),
        queries = kodein.direct.instance(),
        mutations = kodein.direct.instance()
    )
        .handler()
        .asServer(Jetty(8080))
        .start()
}
