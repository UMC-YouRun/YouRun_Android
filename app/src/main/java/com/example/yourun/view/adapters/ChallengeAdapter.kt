package com.example.yourun.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.ChallengeItem

class ChallengeAdapter(private val challengeList: List<ChallengeItem>) :
    RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    inner class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val badgeImage: ImageView = itemView.findViewById(R.id.badgeImage)
        val title: TextView = itemView.findViewById(R.id.titleText)
        val description: TextView = itemView.findViewById(R.id.descriptionText)
        val remaining: TextView = itemView.findViewById(R.id.remainingText)
        val member1: ImageView = itemView.findViewById(R.id.member1Image)
        val member2: ImageView = itemView.findViewById(R.id.member2Image)
        val member3: ImageView = itemView.findViewById(R.id.member3Image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challengeList[position]
        holder.badgeImage.setImageResource(challenge.badgeImage)
        holder.title.text = challenge.title
        holder.description.text = challenge.description
        holder.remaining.text = challenge.remaining

        // 멤버 아이콘 설정 (멤버가 3명이라고 가정)
        holder.member1.setImageResource(challenge.members[0])
        holder.member2.setImageResource(challenge.members[1])
        holder.member3.setImageResource(challenge.members[2])
    }

    override fun getItemCount(): Int {
        return challengeList.size
    }

    fun updateData(newList: List<ChallengeItem>) {
        (challengeList as? MutableList<ChallengeItem>)?.apply {
            clear()
            addAll(newList)
        }
        notifyDataSetChanged()
    }
}