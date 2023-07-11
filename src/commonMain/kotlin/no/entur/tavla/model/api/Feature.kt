package no.entur.tavla.model.api

import kotlinx.serialization.Serializable

@Serializable
data class Feature(
    val type: String,
    val geometry: Geometry,
    val properties: Properties,
)
