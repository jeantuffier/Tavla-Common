package no.entur.tavla.orchestration

import arrow.core.Either
import com.jeantuffier.statemachine.orchestrate.Available
import com.jeantuffier.statemachine.orchestrate.Limit
import com.jeantuffier.statemachine.orchestrate.Offset
import com.jeantuffier.statemachine.orchestrate.OrchestratedUpdate
import com.jeantuffier.statemachine.orchestrate.Page
import no.entur.tavla.http.GetStopPlaces
import no.entur.tavla.model.CommonTavlaError
import no.entur.tavla.model.api.Feature
import no.entur.tavla.screen.LoadData

internal fun stops(getStopPlaces: GetStopPlaces) =
    OrchestratedUpdate<LoadData, CommonTavlaError, Page<Feature>> {
        Either.catch {
            val response = getStopPlaces(it.latitude, it.longitude)
            Page(
                available = Available(response.features.size),
                offset = Offset(0),
                limit = Limit(5),
                items = response.features
            )
        }.mapLeft { CommonTavlaError.ApiError(it) }
    }