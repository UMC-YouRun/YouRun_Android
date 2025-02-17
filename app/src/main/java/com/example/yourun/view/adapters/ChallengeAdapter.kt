package com.example.yourun.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.ChallengeItem

class ChallengeAdapter(private val challengeList: MutableList<ChallengeItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_ITEM = 0
    private val TYPE_FOOTER = 1
    var isFooterVisible: Boolean = false

    override fun getItemViewType(position: Int): Int {
        return if (position == challengeList.size) TYPE_FOOTER else TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return challengeList.size + if (isFooterVisible) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false)
            FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = challengeList[position]
            holder.title.text = item.title
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_text)
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun updateList(newList: List<ChallengeItem>) {
        Log.d("ADAPTER_DEBUG", "updateList 호출됨, 새 리스트 크기: ${newList.size}")
        for (item in newList) {
            Log.d("ADAPTER_DEBUG", "챌린지 제목: ${item.title}, 남은 시간: ${item.remaining}")
        }
        challengeList.clear() // 기존 데이터 삭제
        challengeList.addAll(newList) // 새로운 데이터 추가
        Log.d("ADAPTER_DEBUG", "업데이트 후 리스트 크기: ${challengeList.size}")
        notifyItemRangeChanged(0, challengeList.size)
    }
}