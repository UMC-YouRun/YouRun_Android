package com.example.yourun.view.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yourun.R
import com.example.yourun.databinding.ActivityMyrun2Binding
import com.example.yourun.model.data.request.UpdateUserRequest
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class MyRun2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyrun2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyrun2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageRes = intent.getIntExtra("profile_image_res", -1)

        if (imageRes != -1) {
            Log.d("MyRun2Activity", "받은 이미지 리소스: $imageRes") // 로그 확인
            binding.ivProfileCharacter.setImageResource(imageRes)
        } else {
            Log.e("MyRun2Activity", "이미지 리소스가 정상적으로 전달되지 않음")
        }


        binding.btnDuplicate.setOnClickListener {
            val nickname = binding.editTextNickname.text.toString()
            checkNicknameDuplicate(nickname)
        }

        binding.btnChange.setOnClickListener {
            val nickname = binding.editTextNickname.text.toString().trim()
            val selectedTags = getSelectedTags()

            if (selectedTags.size < 2) {
                Toast.makeText(this, "태그를 두 개 선택해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tag1 = selectedTags[0]
            val tag2 = selectedTags[1]

            updateUserTagsAndNickname(nickname, tag1, tag2)
        }
    }

    private fun getSelectedTags(): List<String> {
        val selectedTags = mutableListOf<String>()

        if (binding.checkbox1.isChecked) selectedTags.add("느긋하게")
        if (binding.checkbox2.isChecked) selectedTags.add("음악과")
        if (binding.checkbox3.isChecked) selectedTags.add("열정적")
        if (binding.checkbox4.isChecked) selectedTags.add("자기계발")
        if (binding.checkbox5.isChecked) selectedTags.add("에너자이저")
        if (binding.checkbox6.isChecked) selectedTags.add("왕초보")

        return selectedTags
    }

    private fun checkNicknameDuplicate(nickname: String) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.getApiService().checkNicknameDuplicate(nickname)
                if (response != null && response.status == 200 && response.data) {
                    binding.nicknameContent.apply {
                        text = "사용 가능한 닉네임입니다."
                        setTextColor(ContextCompat.getColor(this@MyRun2Activity, R.color.purple))
                    }
                    binding.btnDuplicate.setImageResource(R.drawable.img_again_check_color)
                } else {
                    binding.nicknameContent.apply {
                        text = "이미 사용 중인 닉네임입니다."
                        setTextColor(ContextCompat.getColor(this@MyRun2Activity, R.color.red))
                    }
                    binding.btnDuplicate.setImageResource(R.drawable.img_againcheck)
                }
            } catch (e: Exception) {
                Toast.makeText(this@MyRun2Activity, "에러가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserTagsAndNickname(nickname: String, tag1: String, tag2: String) {
        lifecycleScope.launch {
            try {
                val request = UpdateUserRequest(nickname, tag1, tag2)
                val response = ApiClient.getApiService().updateUserTagsAndNickname(request)
                if (response != null && response.status == 200 ) {
                    Toast.makeText(this@MyRun2Activity, "정보가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MyRun2Activity, "업데이트 실패: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MyRun2Activity, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

