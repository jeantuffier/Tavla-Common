package no.entur.tavla.api

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import no.entur.tavla.DepartureBoardQuery

internal fun interface GetDepartureBoard {
    suspend operator fun invoke(
        ids: List<String>,
    ): Either<ApiError.FailedToGetDepartureBoard, DepartureBoardQuery.Data>
}

internal fun getDepartureBoard(apolloClient: ApolloClient) = GetDepartureBoard {
    Either.catch {
        apolloClient.query(DepartureBoardQuery(it))
            .execute()
            .data ?: throw Exception("Departure board cannot be empty")
    }.mapLeft { ApiError.FailedToGetDepartureBoard }
}

