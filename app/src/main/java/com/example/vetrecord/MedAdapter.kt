package com.example.vetrecord

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedAdapter(
    private var list: ArrayList<Medication>,
    private val dbHelper: DBHelper,
    private val refresh: () -> Unit
) : RecyclerView.Adapter<MedAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.tvName)
        val dose: TextView = v.findViewById(R.id.tvDose)
        val date: TextView = v.findViewById(R.id.tvDate)
        val menu: ImageView = v.findViewById(R.id.btnMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medication, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        val item = list[position]

        holder.name.text = item.name
        holder.dose.text = item.dosage
        holder.date.text = item.date

        holder.menu.setOnClickListener { view ->

            val popup = PopupMenu(view.context, view)
            popup.menu.add(0, 1, 0, "Edit")
            popup.menu.add(0, 2, 1, "Delete")

            popup.setOnMenuItemClickListener {

                when (it.itemId) {

                    2 -> {
                        dbHelper.deleteMedication(item.id)
                        list.removeAt(position)
                        notifyItemRemoved(position)
                        refresh()
                    }

                    1 -> {
                        val context = view.context

                        val name = EditText(context).apply {
                            setText(item.name)
                            hint = "Name"
                        }

                        val dose = EditText(context).apply {
                            setText(item.dosage)
                            hint = "Dose"
                        }

                        val layout = LinearLayout(context).apply {
                            orientation = LinearLayout.VERTICAL
                            setPadding(40, 20, 40, 10)
                            addView(name)
                            addView(dose)
                        }

                        AlertDialog.Builder(context)
                            .setTitle("Edit Medication")
                            .setView(layout)
                            .setPositiveButton("Save") { _, _ ->
                                dbHelper.updateMedication(
                                    item.id,
                                    name.text.toString(),
                                    dose.text.toString()
                                )
                                refresh()
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                }
                true
            }

            popup.show()
        }
    }

    override fun getItemCount(): Int = list.size
}