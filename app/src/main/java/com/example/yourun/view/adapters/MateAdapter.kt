package com.example.yourun.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.response.Mate


class MateAdapter(private val mateList: List<Mate>) :
    RecyclerView.Adapter<MateAdapter.MateViewHolder>() {

    class MateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rank: TextView = itemView.findViewById((R.id.idx_challenge_item))
        val profileImage: ImageView = itemView.findViewById(R.id.img_user_profile)
        val name: TextView = itemView.findViewById(R.id.user_name)
        val runday: TextView = itemView.findViewById(R.id.user_runday)
        val change: TextView = itemView.findViewById(R.id.mate_change)
        val distance: TextView = itemView.findViewById(R.id.user_km)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mate, parent, false)
        return MateViewHolder(view)
    }

    override fun onBindViewHolder(holder: MateViewHolder, position: Int) {
        val mate = mateList[position]
        holder.rank.text = mate.rank.toString()
        holder.profileImage.setImageResource(mate.profileImageResId)
        holder.name.text = mate.name
        holder.runday.text = "${mate.runday}일째"
        holder.distance.text = "${mate.distance}km"
        holder.change.text = "${mate.change}위"
    }

    override fun getItemCount(): Int {
        return mateList.size
    }
}
