package no.entur.tavla.screen

import arrow.core.right
import com.jeantuffier.statemachine.orchestrate.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import no.entur.tavla.DepartureBoardQuery
import no.entur.tavla.model.api.*
import no.entur.tavla.model.screen.*
import no.entur.tavla.screen.homeScreen.HomeScreenAction
import no.entur.tavla.screen.homeScreen.HomeScreenState
import no.entur.tavla.screen.homeScreen.homeScreenStateMachine
import no.entur.tavla.type.TransportMode
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenTest {

    private fun features() = (0..10).map {
        Feature(
            type = it.toString(),
            geometry = Geometry(
                type = it.toString(),
                coordinates = listOf(),
            ),
            properties = Properties(
                id = it.toString(),
                gid = it.toString(),
                layer = Layer.venue,
                source = Source.whosonfirst,
                sourceId = it.toString(),
                name = it.toString(),
                street = it.toString(),
                confidence = 1f,
                distance = 1f,
                accuracy = it.toString(),
                country = it.toString(),
                county = it.toString(),
                countyGid = it.toString(),
                locality = it.toString(),
                localityGid = it.toString(),
                label = it.toString(),
                category = listOf(),
                tariffZone = listOf(),
            ),
        )
    }

    private fun data(index: Int) = DepartureBoardQuery.Data(
        stopPlaces = listOf(
            DepartureBoardQuery.StopPlace(
                id = index.toString(),
                name = index.toString(),
                estimatedCalls = listOf(
                    DepartureBoardQuery.EstimatedCall(
                        realtime = false,
                        aimedArrivalTime = index.toString(),
                        expectedArrivalTime = index.toString(),
                        aimedDepartureTime = index.toString(),
                        expectedDepartureTime = index.toString(),
                        actualArrivalTime = index.toString(),
                        actualDepartureTime = index.toString(),
                        date = index.toString(),
                        forBoarding = false,
                        forAlighting = false,
                        destinationDisplay = DepartureBoardQuery.DestinationDisplay(index.toString()),
                        quay = DepartureBoardQuery.Quay(index.toString()),
                        serviceJourney = DepartureBoardQuery.ServiceJourney(
                            journeyPattern = DepartureBoardQuery.JourneyPattern(
                                line = DepartureBoardQuery.Line(
                                    id = index.toString(),
                                    transportMode = TransportMode.bus,
                                    name = index.toString(),
                                ),
                            ),
                        ),
                    )
                ),
            ),
        ),
    )

    private fun stopPlaceData() = List(features().size) { _ ->
        StopPlaceData(
            id = "",
            categories = listOf(StopPlaceCategory.busStation, StopPlaceCategory.metroStation),
            name = "Test station",
            distanceFromUser = 0.1f,
            departures = mapOf(
                "1" to Departures(
                    transportMode = TransportMode.bus,
                    title = "bus 1",
                    aimedDepartureTimes = listOf("10:00"),
                    actualDepartureTimes = listOf("10:10"),
                )
            ),
        )
    }

    @Test
    fun shouldLoadStops() = runTest {
        val stopPlaceData = stopPlaceData()
        val stateMachine = homeScreenStateMachine(
            stops = {
                delay(100)
                Page(
                    available = Available(10),
                    offset = Offset(0),
                    limit = Limit(10),
                    items = stopPlaceData,
                ).right()
            },
            coroutineDispatcher = StandardTestDispatcher(testScheduler),
            initialValue = HomeScreenState(),
        )

        assertEquals(HomeScreenState(), stateMachine.state.value)

        stateMachine.reduce(HomeScreenAction.LoadData(1f, 1f, Limit(10), Offset(0)))

        advanceTimeBy(10)

        assertEquals(
            expected = HomeScreenState(
                stops = OrchestratedPage(
                    available = Available(0),
                    isLoading = true,
                    pages = mapOf(),
                )
            ),
            actual = stateMachine.state.value,
        )

        advanceTimeBy(100)

        assertEquals(
            expected = HomeScreenState(
                stops = OrchestratedPage(
                    available = Available(10),
                    isLoading = false,
                    pages = mapOf(0 to stopPlaceData),
                )
            ),
            actual = stateMachine.state.value,
        )
    }
}