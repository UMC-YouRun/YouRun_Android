package com.example.yourun.view.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.yourun.R
import com.example.yourun.viewmodel.ChallengeViewModel
import com.example.yourun.viewmodel.SoloChallengeDetailViewModel

class SoloChallengeDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: SoloChallengeDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_challenge_detail)

        val challengeId = intent.getLongExtra("CHALLENGE_ID", -1)

        if (challengeId == -1L) {
            Log.e("ERROR", "잘못된 CHALLENGE_ID 값")
            finish()
        } else {
            Log.d("DEBUG", "받은 CHALLENGE_ID: $challengeId")
        }
    }
}