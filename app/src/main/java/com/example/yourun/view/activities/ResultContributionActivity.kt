package com.example.yourun.view.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R

class ResultContributionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_crew_contribution)

        val topBarTitle: TextView = findViewById(R.id.txt_top_bar_with_back_button)
        topBarTitle.text = "크루 챌린지 결과"

        val layoutToggle = findViewById<LinearLayout>(R.id.layout_toggle)
        val layoutContributionList = findViewById<LinearLayout>(R.id.layout_contribution_list)
        val imgToggleArrow = findViewById<ImageView>(R.id.img_toggle_arrow)

        layoutToggle.setOnClickListener {
            if (layoutContributionList.visibility == View.GONE) {
                layoutContributionList.visibility = View.VISIBLE
                imgToggleArrow.setImageResource(R.drawable.ic_arrow_up)
            } else {
                layoutContributionList.visibility = View.GONE
                imgToggleArrow.setImageResource(R.drawable.ic_arrow_down)
            }
        }
    }
}
