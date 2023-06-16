package no.entur.tavla.model.api

data class Language(
    val name: String,
    val iso6391: String,
    val iso6393: String,
    val defaulted: Boolean,
)
