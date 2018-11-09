package pl.jitsolutions.agile.presentation.daily

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.utils.mutableLiveData

class DailyViewModel(dispatcher: CoroutineDispatcher) : CoroutineViewModel(dispatcher) {

    val users = MutableLiveData<List<User>>()
    val project = mutableLiveData("Project name")
    val dailyState = mutableLiveData<DailyState>(DailyState.Prepare)

    init {
        val userList = arrayListOf(
            User("id", "Marek", "marek@o2.pl"),
            User("id", "Andrzej", "andrzej@o2.pl"),
            User("id", "Bronek", "bronek@o2.pl")
        )
        users.value = userList
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
}