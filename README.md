# graphql4k

GraphQL request handler for http4k 

The following sample requires ```org.http4k:http4k-server-jetty``` dependency in order to be executed : 
```
GraphQL4k(
           supportedPackages = listOf("com.graphql4k.api"),
           queries = listOf(PersonQuery(), ProjectQuery()),
           mutations = listOf(PersonMutation(), ProjectMutation()),
       )
           .httpHandler()
           .asServer(Jetty(8080))
           .start()
```

Another sample with the little help of a DI engine (kodein) : 

```
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
        .httpHandler()
        .asServer(Jetty(8080))
        .start()
``` 
