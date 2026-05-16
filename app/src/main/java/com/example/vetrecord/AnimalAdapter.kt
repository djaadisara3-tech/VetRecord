package com.example.vetrecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.widget.Button


class AnimalAdapter(
    private var list: ArrayList<Animal>
) : RecyclerView.Adapter<AnimalAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val name: TextView =
            view.findViewById(R.id.txtName)

        val species: TextView =
            view.findViewById(R.id.txtSpecies)

        val owner: TextView =
            view.findViewById(R.id.txtOwner)

        val btnProfile: Button =
            view.findViewById(R.id.btnProfile)

        val btnEdit: Button =
            view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val animal = list[position]

        holder.name.text = animal.name
        holder.species.text = "Species: ${animal.species}"
        holder.owner.text = "Owner: ${animal.owner}"

        holder.btnProfile.setOnClickListener {

            val i = Intent(
                holder.itemView.context,
                PetProfileActivity::class.java
            )

            i.putExtra(
                "animal_id",
                animal.id
            )

            holder.itemView.context.startActivity(i)
        }
    }

    fun updateList(newList: ArrayList<Animal>) {
        list = newList
        notifyDataSetChanged()
    }
}