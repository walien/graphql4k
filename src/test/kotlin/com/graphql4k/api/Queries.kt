package com.graphql4k.api

import com.graphql4k.Query

class ProjectQuery : Query {

    fun findAllProjects() = listOf(Project("Sample1"), Project("Sample2"))
}

class PersonQuery : Query {

    fun findAllPersons() = listOf(Person(firstName = "John"), Person(firstName = "Jerry"))
}
