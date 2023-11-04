package no.entur.tavla.orchestration

import arrow.core.Either
import arrow.core.right
import com.jeantuffier.statemachine.orchestrate.Limit
import com.jeantuffier.statemachine.orchestrate.Offset
import kotlinx.coroutines.test.runTest
import no.entur.tavla.DepartureBoardQuery
import no.entur.tavla.model.api.Engine
import no.entur.tavla.model.api.Feature
import no.entur.tavla.model.api.GeoCoding
import no.entur.tavla.model.api.Geometry
import no.entur.tavla.model.api.Language
import no.entur.tavla.model.api.Layer
import no.entur.tavla.model.api.Properties
import no.entur.tavla.model.api.Query
import no.entur.tavla.model.api.Source
import no.entur.tavla.model.api.StopPlaceResponse
import no.entur.tavla.screen.homeScreen.HomeScreenAction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeScreenDataTest {

    private fun features(id: Int) = Feature(
        type = id.toString(),
        geometry = Geometry(
            type = id.toString(),
            coordinates = listOf(),
        ),
        properties = Properties(
            id = id.toString(),
            gid = id.toString(),
            layer = Layer.venue,
            source = Source.whosonfirst,
            sourceId = id.toString(),
            name = id.toString(),
            street = id.toString(),
            confidence = 1f,
            distance = 1f,
            accuracy = id.toString(),
            country = id.toString(),
            county = id.toString(),
            countyGid = id.toString(),
            locality = id.toString(),
            localityGid = id.toString(),
            label = id.toString(),
            category = listOf(),
            tariffZone = listOf(),
        ),
    )

    private fun departureBoardStopPLace(id: String) = DepartureBoardQuery.StopPlace(
        id = id,
        name = id,
        estimatedCalls = listOf(),
    )

    @Test
    fun shouldLoadData() = runTest {
        val result = homeScreenData(
            getStopPlaces = { _, _ ->
                StopPlaceResponse(
                    geocoding = GeoCoding(
                        version = "",
                        attribution = "",
                        query = Query(
                            layers = listOf(),
                            sources = listOf(),
                            size = 0,
                            private = false,
                            pointLatitude = 0f,
                            pointLongitude = 0f,
                            radius = 0,
                            circleLatitude = 0f,
                            circleLongitude = 0f,
                            lang = Language(
                                name = "",
                                iso6391 = "",
                                iso6393 = "",
                                defaulted = false,
                            ),
                            querySize = 0,
                        ),
                        engine = Engine(
                            name = "",
                            author = "",
                            version = "",
                        ),
                        timestamp = 0L,
                    ),
                    type = "type",
                    features = listOf(
                        features(1),
                        features(2),
                        features(3),
                        features(4),
                    ),
                    bbox = listOf(),
                ).right()
            },
            getDepartureBoard = { ids -> DepartureBoardQuery.Data(ids.map { departureBoardStopPLace(it) }).right() },
        )(
            HomeScreenAction.LoadData(
                latitude = 0f,
                longitude = 0f,
                limit = Limit(10),
                offset = Offset(10),
            )
        )
        assertTrue(result is Either.Right)
        assertEquals(4, result.value.items.size)
    }
}