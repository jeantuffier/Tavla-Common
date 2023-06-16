package no.entur.tavla.screen

import com.jeantuffier.statemachine.orchestrate.Action
import com.jeantuffier.statemachine.orchestrate.PageLoader

@Action
interface LoadData : PageLoader {
    val latitude: Float
    val longitude: Float
}
