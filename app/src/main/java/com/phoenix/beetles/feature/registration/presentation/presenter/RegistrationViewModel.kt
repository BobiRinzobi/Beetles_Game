package com.phoenix.beetles.feature.registration.presentation.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phoenix.beetles.feature.registration.domain.entity.User

class RegistrationViewModel : ViewModel() {

    val _state = MutableLiveData<RegistrationState>(RegistrationState.Initial)
    val state : LiveData<RegistrationState> = _state

    fun loadData() {

//        if (_state.value == RegistrationState.Initial || _state.value == RegistrationState.Loading){
//            return
//        }
        // usecase с запросом к бд и в случае неудачи создаём нового user
        _state.value = RegistrationState.Content

    }



}