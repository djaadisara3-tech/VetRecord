package com.example.vetrecord

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedAdapter(
    private var list: ArrayList<Medication>,
    private val dbHelper: DBHelper,
    private val refresh: () -> Unit
) : RecyclerView.Adapter<MedAdapter.VH>() {

    // =========================
    // VIEW HOLDER
    // =========================
    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.tvName)
        val dose: TextView = v.findViewById(R.id.tvDose)
        val date: TextView = v.findViewById(R.id.tvDate)
        val menu: TextView = v.findViewById(R.id.btnMenu)
    }

    // =========================
    // CREATE VIEW
    // =========================
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medication, parent, false)

        return VH(view)
    }

    // =========================
    // BIND DATA
    // =========================
    override fun onBindViewHolder(holder: VH, position: Int) {

        val item = list[position]

        holder.name.text = "${item.type} : ${item.name}"
        holder.dose.text = item.dosage
        holder.date.text = item.date

        // =========================
        // MENU (3 DOTS)
        // =========================
        holder.menu.setOnClickListener {

            val popup = PopupMenu(holder.itemView.context, holder.menu)
            popup.menu.add("Edit")
            popup.menu.add("Delete")

            popup.setOnMenuItemClickListener { menuItem ->

                when (menuItem.title) {

                    "Delete" -> {
                        dbHelper.deleteMedication(item.id)
                        refresh()
                    }

                    "Edit" -> {

                        val context: Context = holder.itemView.context

                        val inputName = EditText(context)
                        inputName.setText(item.name)

                        val inputDose = EditText(context)
                        inputDose.setText(item.dosage)

                        val layout = LinearLayout(context)
                        layout.orientation = LinearLayout.VERTICAL
                        layout.addView(inputName)
                        layout.addView(inputDose)

                        AlertDialog.Builder(context)
                            .setTitle("Edit Medication")
                            .setView(layout)
                            .setPositiveButton("Save") { _, _ ->

                                dbHelper.updateMedication(
                                    item.id,
                                    inputName.text.toString(),
                                    inputDose.text.toString()
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

    // =========================
    // SIZE
    // =========================
    override fun getItemCount(): Int = list.size
}