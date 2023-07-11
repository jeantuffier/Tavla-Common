package no.entur.tavla.screen

import arrow.core.right
import com.jeantuffier.statemachine.orchestrate.Available
import com.jeantuffier.statemachine.orchestrate.Limit
import com.jeantuffier.statemachine.orchestrate.Offset
import com.jeantuffier.statemachine.orchestrate.OrchestratedPage
import com.jeantuffier.statemachine.orchestrate.Page
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import no.entur.tavla.model.api.Feature
import no.entur.tavla.model.api.Geometry
import no.entur.tavla.model.api.Layer
import no.entur.tavla.model.api.Properties
import no.entur.tavla.model.api.Source
import no.entur.tavla.screen.homeScreen.HomeScreenAction
import no.entur.tavla.screen.homeScreen.HomeScreenState
import no.entur.tavla.screen.homeScreen.homeScreenStateMachine
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

    @Test
    fun shouldLoadStops() = runTest {
        val features = features()
        val stateMachine = homeScreenStateMachine(
            stops = {
                delay(100)
                Page(
                    available = Available(10),
                    offset = Offset(0),
                    limit = Limit(10),
                    items = features,
                ).right()
            },
            coroutineDispatcher = StandardTestDispatcher(testScheduler),
            initialValue = HomeScreenState()
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
                    pages = mapOf(0 to features),
                )
            ),
            actual = stateMachine.state.value,
        )
    }
}