package com.example.inventory.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
@InstallIn(SingletonComponent::class)
object SchedulerModule {

    @Provides
    @SchedulersQualifiers.IoScheduler
    fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @SchedulersQualifiers.ComputationScheduler
    fun provideComputationScheduler(): Scheduler {
        return Schedulers.computation()
    }

    @Provides
    @SchedulersQualifiers.UiScheduler
    fun provideUiScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    fun provideSchedulerProvider(
        @SchedulersQualifiers.IoScheduler ioScheduler: Scheduler,
        @SchedulersQualifiers.ComputationScheduler computationScheduler: Scheduler,
        @SchedulersQualifiers.UiScheduler uiScheduler: Scheduler
    ): BaseSchedulerProvider {
        return SchedulerProvider(ioScheduler, computationScheduler, uiScheduler)
    }
}
