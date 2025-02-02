package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yourun.model.data.SignUpRequest

class SignUpViewModel : ViewModel() {

    private val _signUpData = MutableLiveData<SignUpRequest>()
    val signUpData: LiveData<SignUpRequest> = _signUpData

    fun setSignUpData(email: String, password: String, passwordCheck: String) {
        _signUpData.value = SignUpRequest(
            email = email,
            password = password,
            passwordCheck = passwordCheck,
            nickname = "",
            tendency = "",
            tag1 = "",
            tag2 = ""
        )
    }

    fun updateNicknameAndTendency(nickname: String, tendency: String) {
        _signUpData.value = _signUpData.value?.copy(
            nickname = nickname,
            tendency = tendency
        )
    }

    fun updateTags(tag1: String, tag2: String) {
        _signUpData.value = _signUpData.value?.copy(
            tag1 = tag1,
            tag2 = tag2
        )
    }
}
