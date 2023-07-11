package no.entur.tavla.di

import com.jeantuffier.statemachine.core.StateMachine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import no.entur.tavla.http.getStopPlaces
import no.entur.tavla.orchestration.stops
import no.entur.tavla.screen.homeScreen.HomeScreenAction
import no.entur.tavla.screen.homeScreen.HomeScreenState
import no.entur.tavla.screen.homeScreen.homeScreenStateMachine
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeModule = module {
    single<StateMachine<HomeScreenAction, HomeScreenState>>(named("homeScreenStatemachine")) {
        homeScreenStateMachine(
            stops = stops(getStopPlaces(get())),
            initialValue = HomeScreenState(),
            coroutineDispatcher = Dispatchers.IO,
        )
    }
}