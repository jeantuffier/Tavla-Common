package no.entur.tavla.di

import io.ktor.client.HttpClient
import no.entur.tavla.http.httpClient
import org.koin.dsl.module

val commonModule = module {
    single<HttpClient> {
        httpClient()
    }
}