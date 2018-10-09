package pl.jitsolutions.agile.repository

import androidx.lifecycle.LiveData
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.utils.mutableLiveData

class MockUserRepository : UserRepository {
    override fun login(email: String, password: String): LiveData<User> {
        return mutableLiveData(User(email))
    }
}