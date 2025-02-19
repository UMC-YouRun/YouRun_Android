package com.example.yourun.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.repository.LoginRepository
import com.example.yourun.model.data.response.LoginResponse
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>() // 로그인 결과 관리
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response: Response<LoginResponse> = repository.login(email, password)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == 200 && body.data != null) {
                        ApiClient.TokenManager.saveToken(body.data.accessToken) // JWT 저장
                        Log.d("LoginViewModel", "로그인 성공: ${body.data.accessToken}")

                        _loginResult.value = Result.success(body) // 전체 응답 저장
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

    // 카카오 로그인 함수
    /* fun kakaoLogin(kakaoAccessToken: String) {
        viewModelScope.launch {
            try {
                // Repository의 loginWithKakao 함수는 카카오 액세스 토큰을 받아 백엔드의 카카오 로그인 API를 호출한다고 가정합니다.
                val response: Response<LoginResponse> = repository.loginWithKakao(kakaoAccessToken)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == 200 && body.data != null) {
                        // 서버에서 발급한 JWT를 저장
                        ApiClient.TokenManager.saveToken(body.data.accessToken)
                        Log.d("LoginViewModel", "카카오 로그인 성공: ${body.data.accessToken}")

                        _loginResult.value = Result.success(body)
                    } else {
                        _loginResult.value =
                            Result.failure(Exception(body?.message ?: "카카오 로그인 실패"))
                    }
                } else {
                    _loginResult.value = Result.failure(Exception("서버 오류(${response.code()})"))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
            }
        }
    }*/
}
