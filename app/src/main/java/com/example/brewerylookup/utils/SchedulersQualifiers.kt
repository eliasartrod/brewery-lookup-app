package com.example.inventory.utils

import javax.inject.Qualifier

class SchedulersQualifiers {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoScheduler

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ComputationScheduler

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UiScheduler
}