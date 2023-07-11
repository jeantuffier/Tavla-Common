package no.entur.tavla.model

sealed class CommonTavlaError : Throwable() {

    data class ApiError(val error: Throwable) : CommonTavlaError()
}