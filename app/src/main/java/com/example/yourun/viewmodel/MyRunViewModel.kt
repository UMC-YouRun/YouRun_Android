package com.example.yourun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.response.UserInfo
import com.example.yourun.model.repository.MyRunRepository
import kotlinx.coroutines.launch

class MyRunViewModel(private val repository: MyRunRepository) : ViewModel() {
    private val _userInfo = MutableLiveData<UserInfo?>()
    val userInfo: LiveData<UserInfo?> get() = _userInfo

    fun fetchMyRunData() {
        viewModelScope.launch {
            _userInfo.value = repository.getMyRunData()?.data
        }
    }
}
