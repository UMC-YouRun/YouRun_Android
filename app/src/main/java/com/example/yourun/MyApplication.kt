package com.example.yourun

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        val kakaoAppKey: String = BuildConfig.KAKAO_NATIVE_APP_KEY
        KakaoSdk.init(this, kakaoAppKey)
        KakaoMapSdk.init(this, kakaoAppKey)
    }
}