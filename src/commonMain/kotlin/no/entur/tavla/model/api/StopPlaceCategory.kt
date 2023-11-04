package no.entur.tavla.model.api

enum class StopPlaceCategory {
    onstreetBus,
    onstreetTram,
    airport,
    railStation,
    metroStation,
    busStation,
    coachStation,
    tramStation,
    harbourPort,
    ferryPort,
    ferryStop,
    liftStation,
    vehicleRailInterchange,
}

fun StopPlaceCategory.isBusStop() = this == StopPlaceCategory.onstreetBus ||
        this == StopPlaceCategory.busStation

fun StopPlaceCategory.isFerryStop() = this == StopPlaceCategory.ferryStop ||
        this == StopPlaceCategory.ferryPort

fun StopPlaceCategory.isMetroStation() = this == StopPlaceCategory.metroStation

fun StopPlaceCategory.isTramStop() = this == StopPlaceCategory.onstreetTram ||
        this == StopPlaceCategory.tramStation