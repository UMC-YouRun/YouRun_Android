package com.example.yourun.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yourun.model.data.SignUpRequest

data class SignUpData(
    var email: String? = null,
    var password: String? = null,
    var passwordcheck: String? = null,
    var nickname: String? = null,
    var tendency: String? = null,
    var tag1: String? = null,
    var tag2: String? = null,
)

class SignUpViewModel : ViewModel() {
    private val _signUpData = MutableLiveData(SignUpData())
    val signUpData: LiveData<SignUpData> get() = _signUpData

    fun setEmailAndPassword(email: String, password: String, passwordcheck: String) {
        _signUpData.value = _signUpData.value?.copy(
            email = email,
            password = password,
            passwordcheck = passwordcheck
        )
    }

    fun setNickname(nickname: String) {
        _signUpData.value = _signUpData.value?.copy(nickname = nickname)
    }

    fun setTags(tag1: String, tag2: String) {
        _signUpData.value = _signUpData.value?.copy(tag1 = tag1, tag2 = tag2)
    }

    fun setTendency(tendency: String) {
        _signUpData.value = _signUpData.value?.copy(tendency = tendency)
    }

    fun getFinalData(): SignUpRequest {
        val data = _signUpData.value ?: SignUpData()

        return SignUpRequest(
            email = data.email ?: "",
            password = data.password ?: "",
            passwordcheck = data.passwordcheck ?: "",
            nickname = data.nickname ?: "",
            tendency = data.tendency ?: "",
            tag1 = data.tag1 ?: "",
            tag2 = data.tag2 ?: ""
        )
    }

}



