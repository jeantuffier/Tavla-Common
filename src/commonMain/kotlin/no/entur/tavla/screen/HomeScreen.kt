package no.entur.tavla.screen

import com.jeantuffier.statemachine.orchestrate.LoadingStrategy
import com.jeantuffier.statemachine.orchestrate.Orchestrated
import com.jeantuffier.statemachine.orchestrate.OrchestratedPage
import com.jeantuffier.statemachine.orchestrate.Orchestration
import no.entur.tavla.model.CommonTavlaError
import no.entur.tavla.model.api.Feature

@Orchestration(
    baseName = "HomeScreen",
    errorType = CommonTavlaError::class,
    actions = [],
)
interface HomeScreen {

    @Orchestrated(
        trigger = LoadData::class,
        loadingStrategy = LoadingStrategy.SUSPEND,
    )
    val stops: OrchestratedPage<Feature>
}