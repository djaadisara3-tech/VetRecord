package com.example.vetrecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecordAdapter(
    private val list: ArrayList<Record>
) : RecyclerView.Adapter<RecordAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val type: TextView = v.findViewById(R.id.tvType)
        val desc: TextView = v.findViewById(R.id.tvDesc)
        val date: TextView = v.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        val item = list[position]

        holder.type.text = item.type
        holder.desc.text = item.description
        holder.date.text = item.date
    }

    override fun getItemCount(): Int = list.size
}