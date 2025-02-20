package com.example.yourun.view.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.databinding.ActivityRunningResultBinding
import com.example.yourun.model.data.request.RunningResultRequest
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.network.ApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import kotlinx.coroutines.launch
import java.util.Locale

class RunningResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunningResultBinding
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // API 인스턴스 생성
        apiService = ApiClient.getApiService()

        // 카카오 맵 초기화
        mapView = binding.kakaoMap
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupMapView()

        // Intent에서 데이터 받기
        val userRunningDistance = intent.getDoubleExtra("user_running_distance", 0.0) // km 단위
        val userRunningDistanceMeters = (userRunningDistance * 1000).toInt()
        val runningTime = intent.getIntExtra("running_time", 0)
        val avgSpeed = intent.getStringExtra("average_speed") ?: "0.00 /km"
        val mateNickname = intent.getStringExtra("mate_nickname") ?: ""
        val distanceDifference = intent.getDoubleExtra("distance_difference", 0.0) // km
        val startTime = intent.getStringExtra("startTime") ?: ""
        val endTime = intent.getStringExtra("endTime") ?: ""
        val targetTime = intent.getIntExtra("targetTime", 0)
        val formattedTargetTime = targetTime * 60

        // UI 업데이트
        // 1. 사용자 러닝 완료 문구
        val completeText = String.format(Locale.US, "%.2fkm 러닝 완료!", userRunningDistance)
        binding.txtRunningComplete.text = completeText

        // 2. 러닝 시간 (txtResultTime) 업데이트
        val completeText2 = String.format(Locale.US, "$runningTime\"", userRunningDistance)
        binding.txtResultTime.text = completeText2

        // 3. 평균 속도 (txtResultDistance) 업데이트
        binding.txtResultDistance.text = applySpannable(avgSpeed, 16, 11)

        // 4. 메이트 닉네임 표시 (txtResultNameMate)
        val mateText = "$mateNickname 보다"
        val spannableMate = SpannableString(mateText)
        val startIndex = mateText.indexOf(mateNickname)
        if (startIndex >= 0) {
            spannableMate.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_purple)),
                startIndex,
                startIndex + mateNickname.length,
                SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.txtResultNameMate.text = spannableMate

        // 5. 메이트와의 거리 차이 (txt_result_distance_mate) 업데이트
        val diffText = if (distanceDifference >= 0)
            String.format(Locale.US, "%.2fkm 더 뛰었어요!", distanceDifference)
        else
            String.format(Locale.US, "%.2fkm 덜 뛰었어요!", kotlin.math.abs(distanceDifference))
        binding.txtResultDistanceMate.text = diffText

        // 6. 상단 타이틀 업데이트
        binding.runningResultTopBar.txtTopBarWithBackButton.text = "러닝 결과"

        // 서버로 데이터 전송
        binding.btnOk.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response = apiService.sendRunningResult(
                        RunningResultRequest(
                            targetTime = formattedTargetTime,
                            startTime = startTime,
                            endTime = endTime,
                            totalDistance = userRunningDistanceMeters
                        )
                    )
                    if (response.isSuccessful) {
                        val body = response.body()
                        val data = body?.data
                        if (data != null) {
                            if (data.isSoloChallengeInProgress && data.isCrewChallengeInProgress) {
                                // 개인, 크루 챌린지 결과
                                val intent = Intent(this@RunningResultActivity, ResultSoloActivity::class.java)
                                intent.putExtra("isCrewChallengeInProgress", true)
                                intent.putExtra("isSoloChallengeInProgress", true)
                                startActivity(intent)
                                finish()
                            } else if (data.isSoloChallengeInProgress) {
                                // 개인 챌린지 결과
                                val intent = Intent(this@RunningResultActivity, ResultSoloActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else if (data.isCrewChallengeInProgress) {
                                // 크루 챌린지 결과
                                val intent = Intent(this@RunningResultActivity, ResultCrewActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // 챌린지 진행 중이 아닐 때
                                val intent = Intent(this@RunningResultActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Log.e("RunningResultActivity", "서버 응답 데이터가 null입니다.")
                        }
                    } else {
                        Log.e("RunningResultActivity", "서버 응답 실패: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("RunningResultActivity", "네트워크 요청 실패: ${e.message}")
                }
            }
        }

        // 백 버튼 클릭 시, 홈 화면으로 이동
        binding.runningResultTopBar.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 캘린더 화면으로 이동
        binding.btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun sendRunningResult(request: RunningResultRequest) {
        lifecycleScope.launch {
            try {
                val response = apiService.sendRunningResult(request)
                if (response.isSuccessful) {
                    Log.d("RunningResultActivity", "러닝 결과 전송 성공")
                } else {
                    Log.e("RunningResultActivity", "서버 응답 에러: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RunningResultActivity", "네트워크 요청 중 오류 발생", e)
            }
        }
    }

    private fun setupMapView() {
        Log.d("KakaoMap", "setupMapView() 호출됨")
        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapResumed() {
                    Log.d("KakaoMap", "onMapResumed() 호출됨")
                    super.onMapResumed()
                }
                override fun onMapDestroy() {
                    Log.d("KakaoMap", "onMapDestroy() 호출됨")
                }
                override fun onMapError(error: Exception?) {
                    error?.printStackTrace()
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    Log.d("KakaoMap", "onMapReady() 호출됨")
                    showCurrentLocation(kakaoMap)
                }
            }
        )
    }

    private fun showCurrentLocation(kakaoMap: KakaoMap) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val locationRequest = LocationRequest.Builder(10000)  // 10 seconds interval
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setMaxUpdates(5)  // Optionally, limit the number of updates
                    .build()

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            val currentLatLng = LatLng.from(it.latitude, it.longitude)
                            var cameraUpdate = CameraUpdateFactory.newCenterPosition(currentLatLng)
                            kakaoMap.moveCamera(cameraUpdate)

                            // 1. LabelStyles 생성하기 - Icon 이미지 하나만 있는 스타일
                            val bitmap = vectorToBitmap(this, R.drawable.ic_map_label)
                            val labelStyle = LabelStyle.from(bitmap)
                            val labelStyles = LabelStyles.from(labelStyle)

                            // 2. LabelOptions 생성하기
                            val labelOptions = LabelOptions.from(currentLatLng)
                                .setStyles(labelStyles)

                            // 3. LabelLayer 가져오기 (또는 커스텀 Layer 생성)
                            val labelManager = kakaoMap.labelManager
                            val labelLayer = labelManager?.layer

                            // 4. LabelLayer에 LabelOptions를 넣어 Label 생성하기
                            var label = labelLayer?.addLabel(labelOptions)

                            // 5. 레이블의 위치를 동적으로 업데이트하는 방법
                            val locationCallback = object : LocationCallback() {
                                override fun onLocationResult(p0: LocationResult) {
                                    super.onLocationResult(p0)
                                    val updatedLocation = p0.lastLocation
                                    updatedLocation?.let {
                                        val updatedLatLng = LatLng.from(it.latitude, it.longitude)

                                        // 6. 기존 레이블 제거 (필요 시)
                                        label?.remove()

                                        // 7. 새로운 레이블 생성하여 추가
                                        label = labelLayer?.addLabel(LabelOptions.from(updatedLatLng).setStyles(labelStyles))

                                        // 카메라를 업데이트하여 사용자의 위치를 중심으로 설정
                                        cameraUpdate = CameraUpdateFactory.newCenterPosition(updatedLatLng)
                                        kakaoMap.moveCamera(cameraUpdate)
                                    }
                                }
                            }

                            // 7. 위치 업데이트 요청
                            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                        }
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                    }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun vectorToBitmap(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId) ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    // UI 텍스트 포맷팅 (Spannable 적용)
    private fun applySpannable(text: String, numberSize: Int, unitSize: Int): SpannableString {
        val spannable = SpannableString(text)
        val splitIndex = text.indexOf(" ")
        if (splitIndex > 0) {
            spannable.setSpan(AbsoluteSizeSpan(numberSize, true), 0, splitIndex, SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(AbsoluteSizeSpan(unitSize, true), splitIndex, text.length, SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannable
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}