package no.entur.tavla.model.api

import kotlinx.serialization.Serializable

@Serializable
data class Engine(
    val name: String,
    val author: String,
    val version: String,
)
