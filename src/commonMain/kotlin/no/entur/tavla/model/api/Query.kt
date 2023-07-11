package no.entur.tavla.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Query(
    val layers: List<Layer>,
    val sources: List<Source>,
    val size: Int,
    val private: Boolean,

    @SerialName("point.lat")
    val pointLatitude: Float,

    @SerialName("point.lon")
    val pointLongitude: Float,

    @SerialName("boundary.circle.radius")
    val radius: Int,

    @SerialName("boundary.circle.lat")
    val circleLatitude: Float,

    @SerialName("boundary.circle.lon")
    val circleLongitude: Float,

    val lang: Language,
    val querySize: Int,
)
