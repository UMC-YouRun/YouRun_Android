package com.example.yourun.view.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.yourun.R
import com.example.yourun.databinding.FragmentRunningBinding
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.RunningRepository
import com.example.yourun.view.activities.CalendarActivity
import com.example.yourun.view.activities.RunningActivity
import com.example.yourun.viewmodel.RunningViewModel
import com.example.yourun.viewmodel.RunningViewModelFactory
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

    private var _binding: FragmentRunningBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RunningViewModel by viewModels {
        RunningViewModelFactory(RunningRepository(ApiClient.getApiService()))
    }

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 초기 상태: 버튼 비활성화
        binding.btnRunningStart.isEnabled = false

        var mateId: Long = 0

        parentFragmentManager.setFragmentResultListener("mateSelectKey", this) { key, bundle ->
            val mateName = bundle.getString("mateName")
            val mateTendency = bundle.getString("mateTendency")
            mateId = bundle.getLong("mateId")

            viewModel.mateName.value = mateName
            viewModel.mateTendency.value = mateTendency
            viewModel.mateId.value = mateId
        }

        parentFragmentManager.setFragmentResultListener("targetTimeKey", this) { key, bundle ->
            val targetTime = bundle.getInt("targetTime")

            viewModel.targetTime.value = targetTime
        }

        // 메이트 닉네임 관찰
        viewModel.mateName.observe(viewLifecycleOwner) { nickname ->
            checkAndEnableStartButton()
            if (!nickname.isNullOrEmpty()) {
                binding.bgdMateSelect.setBackgroundResource(R.drawable.bgd_mate_selected)
                binding.imgQuestionMark.visibility = View.GONE

                // 닉네임 색상 변경
                val fullText = "${nickname}와 함께 러닝하기"
                val spannable = SpannableStringBuilder(fullText).apply {
                    setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.text_purple)),
                        0, nickname.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                binding.txtMateSelect.text = spannable
            }
        }

        // 메이트 성향 관찰
        viewModel.mateTendency.observe(viewLifecycleOwner) { tendency ->
            checkAndEnableStartButton()
            binding.imgMateProfile.setImageResource(getProfileImageRes(tendency))
        }

        // 러닝 시간 데이터 관찰
        viewModel.targetTime.observe(viewLifecycleOwner) { selectedTime ->
            checkAndEnableStartButton()
            val timeText = "${selectedTime}분"
            binding.txtSetRunningTime.text = timeText

            // 버튼 색상 변경
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.custom_rounded_button)?.mutate()
            drawable?.let {
                (it as GradientDrawable).setColor(ContextCompat.getColor(requireContext(), R.color.btn_selected))
                binding.runningTimeSet.background = it
            }
        }

        mapView = binding.kakaoMap
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setupMapView()

        binding.beforeRunningTopBar.txtTopBar.text = "러닝 시작하기"

        binding.bgdMateSelect.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, RunningMateSelectFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.runningTimeSet.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("mateId", mateId)
            }
            val runningTimeFragment = RunningTimeFragment().apply {
                arguments = bundle
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, runningTimeFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.btnCalendar.setOnClickListener {
            val intent = Intent(requireContext(), CalendarActivity::class.java)
            startActivity(intent)
        }

        // 러닝 시작 버튼 클릭 -> RunningActivity로 이동
        binding.btnRunningStart.setOnClickListener {
            val intent = Intent(requireContext(), RunningActivity::class.java).apply {
                putExtra("mate_nickname", viewModel.mateName.value)
                putExtra("target_time", viewModel.targetTime.value)
                putExtra("mate_running_distance", viewModel.mateRunningDistance.value)
                putExtra("mate_running_pace", viewModel.mateRunningPace.value)
            }
            startActivity(intent)
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

    private fun getProfileImageRes(tendency: String?): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.img_circle_profile_facemaker
            "스프린터" -> R.drawable.img_circle_profile_sprinter
            "트레일러너" -> R.drawable.img_circle_profile_trailrunner
            else -> R.drawable.bgd_mate_profile
        }
    }

    // 모든 필수 데이터가 설정되었을 때 btnRunningStart 활성화
    private fun checkAndEnableStartButton() {
        val isMateSelected = !viewModel.mateName.value.isNullOrEmpty()
        val isTendencySelected = !viewModel.mateTendency.value.isNullOrEmpty()
        val isTimeSelected = viewModel.targetTime.value != null

        binding.btnRunningStart.isEnabled = isMateSelected && isTendencySelected && isTimeSelected
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
        _binding = null
    }
}