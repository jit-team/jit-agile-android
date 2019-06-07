package pl.jitsolutions.agile.presentation.common

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class CoroutineViewModel(private val mainDispatcher: CoroutineDispatcher) : ViewModel(),
    CoroutineScope {
    private val compositeJob = Job()
    override val coroutineContext: CoroutineContext
        get() = mainDispatcher + compositeJob

    @CallSuper
    override fun onCleared() {
        compositeJob.cancel()
    }
}