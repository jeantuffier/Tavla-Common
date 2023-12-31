package no.entur.tavla.model.api

import kotlinx.serialization.Serializable

@Serializable
data class GeoCoding(
    val version: String,
    val attribution: String,
    val query: Query,
    val engine: Engine,
    val timestamp: Long,
)
