package no.entur.tavla.model.api

data class StopPlaceResponse(
    val geoCoding: GeoCoding,
    val type: String,
    val features: List<Feature>,
    val bbox: List<Float>
)
