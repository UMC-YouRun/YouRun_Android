package com.example.yourun.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.MateData
import android.graphics.Color
import android.util.Log


class MateRankingAdapter(private val mateDataList: List<MateData>, private val userNickname: String) :
    RecyclerView.Adapter<MateRankingAdapter.MateViewHolder>() {

    class MateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rank: TextView = itemView.findViewById((R.id.idx_challenge_item))
        val profileImage: ImageView = itemView.findViewById(R.id.img_user_profile)
        val nickname: TextView = itemView.findViewById(R.id.user_name)
        val tags: TextView = itemView.findViewById(R.id.user_tag)
        val countDay: TextView = itemView.findViewById(R.id.user_runday)
        val change: TextView = itemView.findViewById(R.id.mate_change)
        val distance: TextView = itemView.findViewById(R.id.user_km)
        val itemLayout: View = itemView.findViewById(R.id.mate_item_layout) // 사용자는 배경색 변경
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mate, parent, false)
        return MateViewHolder(view)
    }

    override fun onBindViewHolder(holder: MateViewHolder, position: Int) {
        val mate = mateDataList[position]
        holder.rank.text = mate.rank.toString()
        holder.profileImage.setImageResource(mate.profileImageResId)
        holder.nickname.text = mate.nickname
        holder.tags.text = mate.tags?.joinToString(", ") ?: ""
        holder.countDay.text = "${mate.countDay}일째"
        val distanceKm = (mate.totalDistance / 1000)
        holder.distance.text = "${mate.totalDistance}km"
        Log.d("MateAdapter", "메이트 거리 값 (변환 후 UI 표시): ${distanceKm}km")
        holder.change.text = "${mate.change}위"
        // 현재 사용자의 닉네임과 리스트의 닉네임이 같다면 노란색 배경 적용
        if (mate.nickname == userNickname) {
            holder.itemLayout.setBackgroundColor(Color.parseColor("#FFDD85")) // 연한 노란색
        } else {
            holder.itemLayout.setBackgroundColor(Color.WHITE) // 기본 흰색 배경
        }
    }

    override fun getItemCount(): Int {
        return mateDataList.size
    }
}
