package no.entur.tavla.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val id: String,
    val gid: String,
    val layer: Layer,
    val source: Source,

    @SerialName("source_id")
    val sourceId: String,

    val name: String,
    val street: String,
    val confidence: Float,
    val distance: Float,
    val accuracy: String,

    @SerialName("country_a")
    val country: String,

    val county: String,

    @SerialName("county_gid")
    val countyGid: String,

    val locality: String,

    @SerialName("locality_gid")
    val localityGid: String,

    val label: String,
    val category: List<Category>,

    @SerialName("tariff_zones")
    val tariffZone: List<String>
)
