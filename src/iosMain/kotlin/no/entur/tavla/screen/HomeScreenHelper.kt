package no.entur.tavla.screen

import com.jeantuffier.statemachine.core.StateMachine
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.StateFlow
import no.entur.tavla.model.screen.StopPlaceData
import no.entur.tavla.screen.homeScreen.HomeScreenAction
import no.entur.tavla.screen.homeScreen.HomeScreenState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class HomeScreenHelper : KoinComponent {
    val stateMachine: StateMachine<HomeScreenAction, HomeScreenState> by inject(named("homeScreenStatemachine"))

    @NativeCoroutinesState
    val state: StateFlow<HomeScreenState> = stateMachine.state

    fun items(): List<StopPlaceData> = state.value.stops.items()
}