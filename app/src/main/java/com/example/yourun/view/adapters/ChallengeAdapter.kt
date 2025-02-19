package com.example.yourun.view.adapters

import android.content.Intent
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.response.ChallengeItem
import com.example.yourun.model.data.response.SoloChallengeItem
import com.example.yourun.view.activities.CrewChallengeDetailActivity
import com.example.yourun.view.activities.SoloChallengeDetailActivity

class CrewChallengeAdapter(private var challengeList: MutableList<ChallengeItem>) :
    RecyclerView.Adapter<CrewChallengeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_crew_challenge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val challenge = challengeList[position]
        Log.d("BIND_DEBUG", "Binding challenge: ${challenge.title} at position $position")
        holder.bind(challenge)
    }

    override fun getItemCount(): Int {
        val size = challengeList.size
        Log.d("DEBUG", "getItemCount 호출됨, 리스트 크기: $size")
        return size
    }

    /* 새로운 데이터로 갱신하는 함수 추가 */
    fun updateData(newChallenges: List<ChallengeItem>) {
        Log.d("UI_DEBUG", "🚀 updateData 호출됨! 새로운 데이터: $newChallenges") // 로그 추가
        challengeList.clear()
        challengeList.addAll(newChallenges)
        Log.d("UI_DEBUG", "업데이트 후 challengeList 사이즈: ${challengeList.size}")

        notifyDataSetChanged()
        Log.d("UI_DEBUG", "🚀 notifyDataSetChanged 호출됨!")
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(challenge: ChallengeItem) {
            Log.d("BIND_DEBUG", "Binding challenge: ${challenge.title}") // ✅ 확인용 로그 추가

            val textViewGoalTitle = itemView.findViewById<TextView>(R.id.tv_goal_title)
            textViewGoalTitle.text = challenge.title  // ✅ UI 반영

            val descriptionText = challenge.description // "~ 크루와 함께!"
            val crewName = descriptionText.substringBefore("과 함께!").substringBefore("와 함께!") // 크루 이름 추출

            val color = ContextCompat.getColor(itemView.context, R.color.purple) // 보라색

            val spannable = SpannableStringBuilder(descriptionText)
            val startIndex = descriptionText.indexOf(crewName)
            val endIndex = startIndex + crewName.length

            spannable.setSpan(
                ForegroundColorSpan(color),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val textView = itemView.findViewById<TextView>(R.id.tv_description)
            textView.text = "" // 기존 텍스트 클리어
            textView.append(spannable) // append() 사용

            itemView.findViewById<TextView>(R.id.tv_remaincount).text = challenge.remaining
            itemView.findViewById<ImageView>(R.id.img_reward_count).setImageResource(challenge.badgeImage)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CrewChallengeDetailActivity::class.java)
                intent.putExtra("challengeId", challenge.challengeId.toString()) // 상세 조회용 ID 전달
                itemView.context.startActivity(intent)
            }
        }
    }
}

class SoloChallengeAdapter(private val challengeList: MutableList<SoloChallengeItem>) :
    RecyclerView.Adapter<SoloChallengeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_solo_challenge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val challenge = challengeList[position]
        Log.d("BIND_DEBUG", "Binding challenge: ${challenge.title}")
        holder.bind(challenge)
    }

    override fun getItemCount(): Int = challengeList.size

    /* 새로운 데이터로 갱신하는 함수 추가 */
    fun updateData(newChallenges: List<SoloChallengeItem>) {
        Log.d("UI_DEBUG", "🚀 updateData 호출됨! 새로운 데이터: $newChallenges") // 로그 추가
        challengeList.clear()
        challengeList.addAll(newChallenges)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(challenge: SoloChallengeItem) {
            Log.d("BIND_DEBUG", "Binding challenge: ${challenge.title}")
            Log.d("BIND_DEBUG", "Challenge creator tendency: ${challenge.challengeCreatorTendency}")
            Log.d("BIND_DEBUG", "Challenge hashtags: ${challenge.hashtags}")

            val textViewGoalTitle = itemView.findViewById<TextView>(R.id.tv_goal_title)
            textViewGoalTitle.text = challenge.title

            val descriptionText = "챌린지 메이트 ${challenge.description}"
            val creatorName = descriptionText.removePrefix("챌린지 메이트 ").substringBefore("과 함께!").substringBefore("와 함께!")

            val color = ContextCompat.getColor(itemView.context, R.color.purple)

            val spannable = SpannableStringBuilder(descriptionText)
            val startIndex = descriptionText.indexOf(creatorName)
            val endIndex = startIndex + creatorName.length

            spannable.setSpan(
                ForegroundColorSpan(color),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val textViewDescription = itemView.findViewById<TextView>(R.id.tv_description)
            textViewDescription.text = ""
            textViewDescription.append(spannable)

            val hashtagTextView = itemView.findViewById<TextView>(R.id.tv_challenge_creator_hashtags)
            hashtagTextView.text = challenge.hashtags

            val profileImageView = itemView.findViewById<ImageView>(R.id.img_challenge_creator)
            val profileImageRes = getProfileImageRes(challenge.challengeCreatorTendency)

            Log.d("BIND_DEBUG", "Setting profile image resource: $profileImageRes for tendency: ${challenge.challengeCreatorTendency}")

            profileImageView.setImageResource(profileImageRes)

            itemView.findViewById<ImageView>(R.id.img_reward_count)
                .setImageResource(challenge.badgeImage)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, SoloChallengeDetailActivity::class.java)
                intent.putExtra("challengeId", challenge.challengeId)
                itemView.context.startActivity(intent)
            }
        }
    }

    fun getProfileImageRes(tendency: String): Int {
        return when (tendency) {
            "스프린터", "SPRINTER" -> R.drawable.img_mini2_sprinter
            "페이스메이커", "PACEMAKER" -> R.drawable.img_mini2_pacemaker
            "트레일러너", "TRAIL_RUNNER" -> R.drawable.img_mini2_trailrunner
            else -> R.drawable.img_mini2_pacemaker
        }
    }
}