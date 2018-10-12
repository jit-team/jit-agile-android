package pl.jitsolutions.agile.presentation

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Job
import kotlin.coroutines.experimental.CoroutineContext

abstract class CoroutineViewModel(private val mainDispatcher: CoroutineDispatcher)
    : ViewModel(), CoroutineScope {
    private val compositeJob = Job()
    override val coroutineContext: CoroutineContext
        get() = mainDispatcher + compositeJob

    @CallSuper
    override fun onCleared() {
        compositeJob.cancel()
    }
}