package no.entur.tavla.model.api

data class Geometry(
    val type: String,
    val coordinates: List<Float>,
)
