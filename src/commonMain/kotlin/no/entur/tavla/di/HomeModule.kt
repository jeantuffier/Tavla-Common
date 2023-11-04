package no.entur.tavla.di

import com.jeantuffier.statemachine.core.StateMachine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import no.entur.tavla.api.getDepartureBoard
import no.entur.tavla.api.getStopPlaces
import no.entur.tavla.orchestration.homeScreenData
import no.entur.tavla.screen.homeScreen.HomeScreenAction
import no.entur.tavla.screen.homeScreen.HomeScreenState
import no.entur.tavla.screen.homeScreen.homeScreenStateMachine
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeModule = module {
    single<StateMachine<HomeScreenAction, HomeScreenState>>(named("homeScreenStatemachine")) {
        homeScreenStateMachine(
            stops = homeScreenData(
                getStopPlaces(get()),
                getDepartureBoard(get())
            ),
            initialValue = HomeScreenState(),
            coroutineDispatcher = Dispatchers.IO,
        )
    }
}