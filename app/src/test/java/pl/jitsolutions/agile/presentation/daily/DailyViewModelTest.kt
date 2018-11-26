package pl.jitsolutions.agile.presentation.daily

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.jitsolutions.agile.awaitResponseMock
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.domain.usecases.EndDailyUseCase
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.domain.usecases.LeaveDailyUseCase
import pl.jitsolutions.agile.domain.usecases.NextDailyUserUseCase
import pl.jitsolutions.agile.domain.usecases.ObserveDailyUseCase
import pl.jitsolutions.agile.domain.usecases.StartDailyUseCase
import pl.jitsolutions.agile.presentation.navigation.Navigator

class DailyViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DailyViewModel

    @Before
    fun `mock DailyViewModel`() {
        val awaitResponseMock = awaitResponseMock(
            response<User?>(
                (User("123", "marek", "marek@jit.team"))
            )
        )
        val getLoggedUserUseCase = mock<GetLoggedUserUseCase> {
            on {
                executeAsync(GetLoggedUserUseCase.Params)
            } doReturn
                awaitResponseMock
        }

        val awaitStartDailyResponseMock = awaitResponseMock(
            response(
                Unit
            )
        )

        val channel = Channel<Response<Daily?>>()
        val observeDailyUseCase = mock<ObserveDailyUseCase> {
            on {
                execute(ObserveDailyUseCase.Params("0"))
            } doReturn
                channel
        }
        val leaveDailyUseCase = mock<LeaveDailyUseCase>()
        val endDailyUseCase = mock<EndDailyUseCase>()
        val startDailyUseCase = mock<StartDailyUseCase> {
            on {
                executeAsync(StartDailyUseCase.Params("0"))
            } doReturn
                awaitStartDailyResponseMock
        }
        val nextDailyUseCase = mock<NextDailyUserUseCase>()
        val navigator = mock<Navigator>()

        viewModel = DailyViewModel(
            observeDailyUseCase = observeDailyUseCase,
            leaveDailyUseCase = leaveDailyUseCase,
            startDailyUseCase = startDailyUseCase,
            endDailyUseCase = endDailyUseCase,
            getLoggedUserUseCase = getLoggedUserUseCase,
            nextDailyUserUseCase = nextDailyUseCase,
            dailyId = "0",
            navigator = navigator,
            dispatcher = Dispatchers.Default
        )
    }

    @Test
    fun `idle daily`() = runBlocking {
        val observerState = mock<Observer<DailyViewModel.State>>()
        viewModel.state.observeForever(observerState)

        val idleDaily =
            Daily(
                Project("0", "test"),
                listOf("123"),
                listOf(
                    User("123", "tester1", "test@test.pl", active = true),
                    User("456", "tester2", "test2@test.pl")
                ),
                "idle",
                0
            )
        viewModel.handleDailyState(idleDaily)

        verify(observerState).onChanged(DailyViewModel.State.InProgress)

        assertEquals("tester1", viewModel.users.value?.get(0)?.name)

        assertEquals(false, viewModel.users.value?.get(0)?.current)

        assertEquals(false, viewModel.users.value?.get(1)?.active)
    }

    @Test
    fun `join daily`() {
        val joinDaily =
            Daily(
                Project("0", "test"),
                listOf("123", "456"),
                listOf(
                    User("123", "marek", "marek@jit.team", active = true),
                    User("456", "tester2", "test2@test.pl", active = true)
                ),
                "idle",
                0
            )

        viewModel.handleDailyState(joinDaily)

        assertEquals(true, viewModel.users.value?.get(1)?.active)
    }

    @Test
    fun `start daily`() {
        val startDaily =
            Daily(
                Project("0", "test"),
                listOf("123", "456"),
                listOf(
                    User("123", "tester1", "test@test.pl", active = true),
                    User("456", "tester2", "test2@test.pl", active = true)
                ),
                "in-progress",
                123456
            )

        viewModel.handleDailyState(startDaily)

        assertEquals(true, viewModel.users.value?.get(0)?.current)
    }
}