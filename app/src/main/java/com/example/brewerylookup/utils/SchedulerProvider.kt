package com.example.inventory.utils

import io.reactivex.Scheduler

class SchedulerProvider(
    private val ioScheduler: Scheduler,
    private val computationScheduler: Scheduler,
    private val uiScheduler: Scheduler
) : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return computationScheduler
    }

    override fun io(): Scheduler {
        return ioScheduler
    }

    override fun ui(): Scheduler {
        return uiScheduler
    }
}