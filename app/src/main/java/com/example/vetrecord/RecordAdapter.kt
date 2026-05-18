package com.example.vetrecord

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecordAdapter(
    private var list: ArrayList<Record>,
    private val dbHelper: DBHelper,
    private val refresh: () -> Unit
) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    // ================= VIEW HOLDER =================
    class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val type: TextView = view.findViewById(R.id.tvType)
        val desc: TextView = view.findViewById(R.id.tvDesc)
        val date: TextView = view.findViewById(R.id.tvDate)

        // ⭐ MENU
        val menu: ImageView = view.findViewById(R.id.btnMenu)
    }

    // ================= CREATE VIEW =================
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record, parent, false)

        return RecordViewHolder(view)
    }

    // ================= BIND =================
    override fun onBindViewHolder(
        holder: RecordViewHolder,
        position: Int
    ) {

        val item = list[position]

        holder.type.text = item.type
        holder.desc.text = item.description
        holder.date.text = item.date

        // ================= MENU =================
        holder.menu.setOnClickListener { view ->

            val popup = PopupMenu(view.context, view)

            popup.menu.add(0, 1, 0, "Edit")
            popup.menu.add(0, 2, 1, "Delete")

            popup.setOnMenuItemClickListener {

                when (it.itemId) {

                    // ================= EDIT =================
                    1 -> {

                        val context = view.context

                        val inputType = EditText(context)
                        inputType.setText(item.type)
                        inputType.hint = "Type"

                        val inputDesc = EditText(context)
                        inputDesc.setText(item.description)
                        inputDesc.hint = "Description"

                        val inputDate = EditText(context)
                        inputDate.setText(item.date)
                        inputDate.hint = "Date"

                        val layout = LinearLayout(context)
                        layout.orientation = LinearLayout.VERTICAL
                        layout.setPadding(40, 20, 40, 10)

                        layout.addView(inputType)
                        layout.addView(inputDesc)
                        layout.addView(inputDate)

                        AlertDialog.Builder(context)
                            .setTitle("Edit Record")
                            .setView(layout)
                            .setPositiveButton("Save") { _, _ ->

                                dbHelper.updateRecord(
                                    item.id,
                                    inputType.text.toString(),
                                    inputDesc.text.toString(),
                                    inputDate.text.toString()
                                )

                                refresh()
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }

                    // ================= DELETE =================
                    2 -> {

                        dbHelper.deleteRecord(item.id)

                        list.removeAt(position)

                        notifyItemRemoved(position)

                        refresh()
                    }
                }

                true
            }

            popup.show()
        }
    }

    // ================= SIZE =================
    override fun getItemCount(): Int = list.size
}