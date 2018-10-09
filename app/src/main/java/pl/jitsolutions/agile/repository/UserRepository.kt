package pl.jitsolutions.agile.repository

import androidx.lifecycle.LiveData
import pl.jitsolutions.agile.domain.User

interface UserRepository {
    fun login(email: String, password: String): LiveData<User>
}