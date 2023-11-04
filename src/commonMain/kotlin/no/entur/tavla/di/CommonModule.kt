package no.entur.tavla.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.LoggingInterceptor
import io.ktor.client.HttpClient
import no.entur.tavla.api.httpClient
import org.koin.dsl.module

val commonModule = module {
    single<HttpClient> {
        httpClient()
    }

    single<ApolloClient> {
        ApolloClient.Builder()
            .serverUrl("https://api.entur.io/journey-planner/v3/graphql")
            .addHttpInterceptor(LoggingInterceptor(level = LoggingInterceptor.Level.BODY))
            .build()
    }
}