package no.entur.tavla.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(listOf(commonModule, homeModule))
    }
}
