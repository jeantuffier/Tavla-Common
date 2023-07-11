package no.entur.tavla.model.api

import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<Float>,
)
