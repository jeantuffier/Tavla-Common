package no.entur.tavla.model.api

data class Feature(
    val type: String,
    val geometry: Geometry,
    val properties: Properties,
)
