package com.example.yourun.view.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.yourun.R
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

class RunningFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_running, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val data = bundle.getString("target_time")

            val timeTextView = view.findViewById<TextView>(R.id.txt_set_running_time)
            timeTextView.text = data
            val runningTimeSetView = view.findViewById<View>(R.id.running_time_set)
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.custom_rounded_button)?.mutate()
            drawable?.let {
                (it as GradientDrawable).setColor(ContextCompat.getColor(requireContext(), R.color.btn_selected))
                runningTimeSetView.background = it
            }
        }

        mapView = view.findViewById(R.id.kakao_map)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setupMapView()

        val topBarView = view.findViewById<View>(R.id.before_running_top_bar)
        val titleTextView = topBarView.findViewById<TextView>(R.id.txt_top_bar)
        titleTextView.text = "러닝 시작하기"

        view.findViewById<View>(R.id.bgd_mate_select).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, RunningMateSelectFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<View>(R.id.running_time_set).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, RunningTimeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupMapView() {
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
                    showCurrentLocation(kakaoMap)
                }
            }
        )
    }

    private fun vectorToBitmap(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId) ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun showCurrentLocation(kakaoMap: KakaoMap) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
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
                            val bitmap = vectorToBitmap(requireContext(), R.drawable.ic_map_label)
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
            Toast.makeText(requireContext(), "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}