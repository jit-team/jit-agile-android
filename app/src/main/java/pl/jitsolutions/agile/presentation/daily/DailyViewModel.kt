package pl.jitsolutions.agile.presentation.daily

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Response.Status.ERROR
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.GetDailyUseCase
import pl.jitsolutions.agile.domain.usecases.LeaveDailyUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.utils.mutableLiveData

class DailyViewModel(
    private val getDailyUseCase: GetDailyUseCase,
    private val leaveDailyUseCase: LeaveDailyUseCase,
    private val dailyId: String,
    private val navigator: Navigator,
    dispatcher: CoroutineDispatcher
) : CoroutineViewModel(dispatcher) {

    val users = MutableLiveData<List<User>>()
    val project = mutableLiveData("")
    val dailyState = mutableLiveData<DailyState>(DailyState.Prepare)
    val state = mutableLiveData<State>(State.Idle)

    init {
        executeGetDaily()
    }

    private fun executeGetDaily() = launch {
        state.value = State.InProgress

        val params = GetDailyUseCase.Params(dailyId)
        val result = getDailyUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> {
                val daily = result.data!!
                project.value = daily.project.name
                dailyState.value = DailyState.Prepare
                users.value = daily.users
            }
            ERROR -> {
                // TODO
            }
        }

        state.value = State.Idle
    }

    fun leaveDaily() = launch {
        state.value = State.InProgress

        val params = LeaveDailyUseCase.Params(dailyId)
        val result = leaveDailyUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> navigator.navigateBack(Navigator.Destination.Daily(dailyId))
            ERROR -> {
                // TODO
            }
        }

        state.value = State.Idle
    }

    fun firstButtonClick() {
        dailyState.value = DailyState.Wait
    }

    fun secondButtonClick() {
        dailyState.value = DailyState.Turn
    }

    sealed class DailyState {
        object Prepare : DailyState()
        object Wait : DailyState()
        object Turn : DailyState()
        object End : DailyState()
    }

    sealed class State {
        object Idle : State()
        object InProgress : State()
    }
}