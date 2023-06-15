package no.entur.tavla.http

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual val httpClientEngineFactory: HttpClientEngineFactory<HttpClientEngineConfig> = CIO