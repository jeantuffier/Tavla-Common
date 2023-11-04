package no.entur.tavla.orchestration

import arrow.core.raise.either
import com.jeantuffier.statemachine.orchestrate.*
import no.entur.tavla.DepartureBoardQuery
import no.entur.tavla.api.ApiError
import no.entur.tavla.api.GetDepartureBoard
import no.entur.tavla.api.GetStopPlaces
import no.entur.tavla.model.screen.Departures
import no.entur.tavla.model.screen.StopPlaceData
import no.entur.tavla.screen.LoadData

internal fun homeScreenData(
    getStopPlaces: GetStopPlaces,
    getDepartureBoard: GetDepartureBoard,
) = OrchestratedUpdate<LoadData, ApiError, Page<StopPlaceData>> { loadData ->
    either {
        val stopPlaces = getStopPlaces(loadData.latitude, loadData.longitude).bind()
        val ids = stopPlaces.features.map { it.properties.id }
        val departureBoards = getDepartureBoard(ids).bind()
        val items = stopPlaces.features.map { feature ->
            val departures = findStopPlaceById(departureBoards, feature.properties.id)
                .asMapOfDestinations()
                .mapValues { it.value.toDepartures() }
            StopPlaceData(
                id = feature.properties.id,
                categories = feature.properties.category,
                name = feature.properties.name,
                distanceFromUser = feature.properties.distance * 1000,
                departures = departures,
            )
        }.sortedBy { it.distanceFromUser }

        Page(
            available = Available(stopPlaces.features.size),
            offset = Offset(0),
            limit = Limit(stopPlaces.features.size),
            items = items
        )
    }
}

private fun findStopPlaceById(
    departureBoards: DepartureBoardQuery.Data,
    id: String,
): DepartureBoardQuery.StopPlace = departureBoards.stopPlaces
    .first { it?.id == id }
    ?: throw ApiError.FailedToGetDepartureBoard

private fun DepartureBoardQuery.StopPlace.asMapOfDestinations() = estimatedCalls
    .groupBy { it.destinationDisplay?.frontText }
    .mapNotNull { (key, value) -> key?.let { it to value } }
    .toMap()

private fun List<DepartureBoardQuery.EstimatedCall>.toDepartures() = Departures(
    transportMode = first().serviceJourney.journeyPattern!!.line.transportMode!!,
    title = departureTitle(first()),
    aimedDepartureTimes = map { it.aimedDepartureTime as String },
)

fun departureTitle(call: DepartureBoardQuery.EstimatedCall): String {
    val destination = call.destinationDisplay?.frontText ?: ""
    val destinationId = call.serviceJourney.journeyPattern?.line?.id
    val last = destinationId?.split(":")?.last()
    return if (last?.toIntOrNull() != null) "$last $destination" else destination
}
