package no.entur.tavla.model.api

import kotlinx.serialization.Serializable

@Serializable
data class StopPlaceResponse(
    val geocoding: GeoCoding,
    val type: String,
    val features: List<Feature>,
    val bbox: List<Float>? = null,
)
