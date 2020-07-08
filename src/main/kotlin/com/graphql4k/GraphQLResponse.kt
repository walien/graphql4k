package com.graphql4k

import graphql.GraphQLError

data class GraphQLResponse<T>(
    val data: T?,
    val errors: List<GraphQLError>?
)
