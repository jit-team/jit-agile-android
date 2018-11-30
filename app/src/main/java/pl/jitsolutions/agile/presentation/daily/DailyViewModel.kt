package pl.jitsolutions.agile.presentation.daily

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.EndDailyUseCase
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.domain.usecases.LeaveDailyUseCase
import pl.jitsolutions.agile.domain.usecases.NextDailyUserUseCase
import pl.jitsolutions.agile.domain.usecases.ObserveDailyUseCase
import pl.jitsolutions.agile.domain.usecases.StartDailyUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.utils.mutableLiveData

class DailyViewModel(
    private val observeDailyUseCase: ObserveDailyUseCase,
    private val leaveDailyUseCase: LeaveDailyUseCase,
    private val startDailyUseCase: StartDailyUseCase,
    private val endDailyUseCase: EndDailyUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val nextDailyUserUseCase: NextDailyUserUseCase,
    private val dailyId: String,
    private val navigator: Navigator,
    dispatcher: CoroutineDispatcher
) : CoroutineViewModel(dispatcher) {

    val users = MutableLiveData<List<User>>()
    val project = mutableLiveData("")
    val startTime = mutableLiveData(0L)
    val dailyState = mutableLiveData<DailyState>(DailyState.Prepare)
    val state = mutableLiveData<State>(State.Idle)
    val playSound = mutableLiveData(false)
    private var queue = emptyList<String>()
    private var userId: String? = null

    init {
        executeGetLoggedUser()
    }

    private fun executeGetLoggedUser() = launch {
        state.value = State.InProgress
        val result = getLoggedUserUseCase.executeAsync(GetLoggedUserUseCase.Params).await()
        when (result) {
            is Success -> {
                userId = result.data!!.id
                executeGetDaily()
            }
            is Failure -> {
            }
        }
        state.value = State.Success
    }

    private fun executeGetDaily() = launch {
        state.value = State.InProgress
        val params = ObserveDailyUseCase.Params(dailyId)
        observeDailyUseCase.execute(params).consumeEach { response ->
            when (response) {
                is Success -> {
                    handleDailyState(response.data)
                    state.value = State.Success
                }
                is Failure -> {
                    TODO()
                }
            }
        }
    }

    private fun handleDailyState(daily: Daily?) {
        if (daily != null) {
            project.value = daily.project.name
            users.value = daily.users
            queue = daily.queue
            if (daily.state == "idle") {
                dailyState.value = DailyState.Prepare
            } else if (daily.state == "in-progress") {
                handleDailyOnAir(daily)
            }
        } else {
            dailyState.value = DailyState.End
            startTime.value = -1L
            observeDailyUseCase.dispose()
        }
    }

    private fun handleDailyOnAir(daily: Daily) {
        val currentUserId = daily.queue[0]
        users.value = sortQueueAndSelectCurrentUser(queue, daily.users)
        dailyState.value = when {
            queue.size == 1 -> if (userId == currentUserId) DailyState.LastTurn else DailyState.LastWait
            userId == currentUserId -> DailyState.Turn
            else -> DailyState.Wait
        }
        startTime.value = daily.startTime
    }

    private fun sortQueueAndSelectCurrentUser(queue: List<String>, users: List<User>): List<User> {
        val orderById = queue
            .asSequence()
            .withIndex()
            .associate { it.value to it.index }
        return users.asSequence()
            .filter { queue.contains(it.id) }
            .sortedBy { orderById[it.id] }
            .map { user ->
                val currentUserId = queue[0]
                user.current = user.id == currentUserId
                user
            }
            .toList()
    }

    fun quitDaily() {
        navigator.navigateBack(Navigator.Destination.Daily(dailyId))
    }

    fun leaveDaily() = launch {
        state.value = State.InProgress
        val params = LeaveDailyUseCase.Params(dailyId)
        val result = leaveDailyUseCase.executeAsync(params).await()
        when (result) {
            is Success -> navigator.navigateBack(Navigator.Destination.Daily(dailyId))
            is Failure -> {
                TODO()
            }
        }
        state.value = State.Success
    }

    fun endDaily() = launch {
        state.value = State.InProgress
        val params = EndDailyUseCase.Params(dailyId)
        val result = endDailyUseCase.executeAsync(params).await()
        when (result) {
            is Success -> {
            }
            is Failure -> {
                TODO()
            }
        }
        state.value = State.Success
    }

    fun nextTurn() {
        state.value = State.InProgress
        when (dailyState.value) {
            DailyState.Prepare -> startDaily()
            DailyState.Wait, DailyState.Turn, DailyState.LastTurn, DailyState.LastWait -> {
                nextTurnDaily()
            }
        }
    }

    private fun startDaily() = launch {
        val params = StartDailyUseCase.Params(dailyId)
        val result = startDailyUseCase.executeAsync(params).await()
        when (result) {
            is Success -> playSound.value = true
            is Failure -> {
                TODO()
            }
        }
        state.value = State.Success
    }

    private fun nextTurnDaily() = launch {
        val params = NextDailyUserUseCase.Params(dailyId)
        val result = nextDailyUserUseCase.executeAsync(params).await()
        when (result) {
            is Success -> {
            }
            is Failure -> {
                TODO()
            }
        }
        state.value = State.Success
    }

    sealed class DailyState {
        object Prepare : DailyState()
        object Wait : DailyState()
        object Turn : DailyState()
        object LastWait : DailyState()
        object LastTurn : DailyState()
        object End : DailyState()
    }

    sealed class State {
        object Success : State()
        object Idle : State()
        object InProgress : State()
    }

    override fun onCleared() {
        super.onCleared()
        observeDailyUseCase.dispose()
    }
}