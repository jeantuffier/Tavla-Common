package no.entur.tavla.model

import kotlinx.serialization.SerialName

data class StopPlaceResponse(
    val geoCoding: GeoCoding,
    val type: String,
    val features: List<Feature>,
    val bbox: List<Float>
)
