package com.example.vetrecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView

class AnimalAdapter(
    private var list: ArrayList<Animal>
) : RecyclerView.Adapter<AnimalAdapter.MyViewHolder>() {

    // =========================================
    // VIEW HOLDER
    // =========================================
    class MyViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val name: TextView =
            view.findViewById(R.id.txtName)

        val species: TextView =
            view.findViewById(R.id.txtSpecies)

        val owner: TextView =
            view.findViewById(R.id.txtOwner)

        val tag: TextView =
            view.findViewById(R.id.txtTag) // 👈 تمت الإضافة هنا

        val btnProfile: Button =
            view.findViewById(R.id.btnProfile)

        val btnEdit: Button =
            view.findViewById(R.id.btnEdit)
    }

    // =========================================
    // CREATE VIEW
    // =========================================
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)

        return MyViewHolder(view)
    }

    // =========================================
    // ITEM COUNT
    // =========================================
    override fun getItemCount(): Int {
        return list.size
    }

    // =========================================
    // BIND DATA
    // =========================================
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val animal = list[position]

        holder.name.text = animal.name
        holder.species.text = "Species: ${animal.species}"
        holder.owner.text = "Owner: ${animal.owner}"

        // 🔥 هنا يتم جلب القيمة من قاعدة البيانات
        holder.tag.text = animal.species

        // =====================================
        // OPEN PROFILE
        // =====================================
        holder.btnProfile.setOnClickListener {

            val intent = Intent(
                holder.itemView.context,
                PetProfileActivity::class.java
            )

            intent.putExtra("animal_id", animal.id)

            holder.itemView.context.startActivity(intent)
        }

        //edit btn
        holder.btnEdit.setOnClickListener {

            val context = holder.itemView.context

            val intent = Intent(context, AddAnimalActivity::class.java)

            intent.putExtra("id", animal.id)
            intent.putExtra("name", animal.name)
            intent.putExtra("species", animal.species)
            intent.putExtra("owner", animal.owner)

            context.startActivity(intent)
        }
    }


    // =========================================
    // UPDATE LIST
    // =========================================
    fun updateList(newList: ArrayList<Animal>) {
        list = newList
        notifyDataSetChanged()
    }
}