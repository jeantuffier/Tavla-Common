package no.entur.tavla.screen

import com.jeantuffier.statemachine.orchestrate.LoadingStrategy
import com.jeantuffier.statemachine.orchestrate.Orchestrated
import com.jeantuffier.statemachine.orchestrate.OrchestratedPage
import com.jeantuffier.statemachine.orchestrate.Orchestration
import no.entur.tavla.api.ApiError
import no.entur.tavla.model.api.Feature
import no.entur.tavla.model.screen.StopPlaceData

@Orchestration(
    baseName = "HomeScreen",
    errorType = ApiError::class,
    actions = [],
)
interface HomeScreen {

    @Orchestrated(
        trigger = LoadData::class,
        loadingStrategy = LoadingStrategy.SUSPEND,
    )
    val stops: OrchestratedPage<StopPlaceData>
}