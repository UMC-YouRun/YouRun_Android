package com.example.yourun.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.repository.LoginRepository
import com.example.yourun.model.data.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<String>>() // 로그인 결과를 관리
    val loginResult: LiveData<Result<String>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response: Response<LoginResponse> = repository.login(email, password)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == 200 && body.data?.access_token != null) {
                        val token = body.data.access_token
                        repository.saveToken(token)
                        Log.d("LoginViewModel", "토큰 저장됨: $token")
                        _loginResult.value = Result.success(token)
                    } else {
                        _loginResult.value = Result.failure(Exception(body?.message ?: "로그인 실패"))
                    }
                } else {
                    _loginResult.value = Result.failure(Exception("서버 오류(${response.code()})"))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
            }
        }
    }
}
