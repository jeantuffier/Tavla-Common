package no.entur.tavla.model.screen

import no.entur.tavla.model.api.StopPlaceCategory
import no.entur.tavla.type.TransportMode

data class Departures(
    val transportMode: TransportMode,
    val title: String,
    val aimedDepartureTimes: List<String>,
)

data class StopPlaceData(
    val id: String,
    val categories: List<StopPlaceCategory>,
    val name: String,
    val distanceFromUser: Float,
    val departures: Map<String, Departures>,
)
