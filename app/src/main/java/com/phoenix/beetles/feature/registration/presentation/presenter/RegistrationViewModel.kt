package com.phoenix.beetles.feature.registration.presentation.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.registration.domain.usecase.GetAllUsersFioUseCase
import com.phoenix.beetles.feature.registration.domain.usecase.InsertUserUseCase
import com.phoenix.beetles.feature.registration.domain.usecase.SetActiveUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val insertUserUseCase: InsertUserUseCase,
    private val getAllUsersFioUseCase: GetAllUsersFioUseCase,
    private val setActiveUserUseCase: SetActiveUserUseCase
) : ViewModel() {

    private val _state = MutableLiveData<RegistrationState>(RegistrationState.Initial)
    val state: LiveData<RegistrationState> = _state

    fun loadData() {
        _state.value = RegistrationState.Loading
        viewModelScope.launch {
            getAllUsersFioUseCase()
                .catch {
                    _state.postValue(RegistrationState.Error)
                }
                .collect { usersList ->
                    val active = usersList.find { it.isActive } ?: usersList.firstOrNull()
                    _state.postValue(
                        RegistrationState.Content(
                            users = usersList,
                            activeUser = active
                        )
                    )
                }
        }
    }

    fun registerUser(user: UserEntity) {
        viewModelScope.launch {
            insertUserUseCase(user)
            setActiveUser(user)
        }
    }

    fun setActiveUser(user: UserEntity) {
        viewModelScope.launch {
            setActiveUserUseCase(user)
        }
    }
}