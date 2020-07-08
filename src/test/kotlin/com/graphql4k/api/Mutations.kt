package com.graphql4k.api

import com.graphql4k.Mutation

class ProjectMutation : Mutation {

    fun updateProject(project: Project): Project = project
}

class PersonMutation : Mutation {

    fun updatePerson(person: Person): Person = person
}
