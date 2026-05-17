package com.example.vetrecord

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PetProfileActivity : AppCompatActivity() {

    lateinit var tvName: TextView
    lateinit var tvSpecies: TextView
    lateinit var tvOwner: TextView

    lateinit var recycler: RecyclerView
    lateinit var adapter: MedAdapter

    lateinit var dbHelper: DBHelper

    lateinit var btnAddMed: FloatingActionButton

    var animalId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_profile)

        // INIT VIEWS
        tvName = findViewById(R.id.tvName)
        tvSpecies = findViewById(R.id.tvSpecies)
        tvOwner = findViewById(R.id.tvOwner)
        recycler = findViewById(R.id.medRecycler)
        btnAddMed = findViewById(R.id.btnAddMed)

        dbHelper = DBHelper(this)

        animalId = intent.getIntExtra("animal_id", -1)

        if (animalId == -1) {
            Toast.makeText(this, "Animal ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadAnimalInfo()
        loadMedications(animalId)

        // =========================
        // ADD MEDICATION DIALOG
        // =========================
        btnAddMed.setOnClickListener {

            val inputName = EditText(this)
            inputName.hint = "Medicine Name"

            val inputDose = EditText(this)
            inputDose.hint = "Dosage (e.g. 5ml)"

            val inputDate = EditText(this)
            inputDate.hint = "Date (2026-05-17)"

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(40, 20, 40, 10)

            layout.addView(inputName)
            layout.addView(inputDose)
            layout.addView(inputDate)

            AlertDialog.Builder(this)
                .setTitle("Add Medication / Vaccine")
                .setView(layout)
                .setPositiveButton("Save") { _, _ ->

                    val db = dbHelper.writableDatabase

                    db.execSQL(
                        """
                        INSERT INTO medications (animal_id, name, dosage, date, type)
                        VALUES (?, ?, ?, ?, ?)
                        """.trimIndent(),
                        arrayOf(
                            animalId,
                            inputName.text.toString(),
                            inputDose.text.toString(),
                            inputDate.text.toString(),
                            "Medicine"
                        )
                    )

                    loadMedications(animalId)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    // =========================
    // LOAD ANIMAL INFO
    // =========================
    private fun loadAnimalInfo() {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT animal_name, species, owner_name FROM animals WHERE animal_id=?",
            arrayOf(animalId.toString())
        )

        if (cursor.moveToFirst()) {
            tvName.text = cursor.getString(0)
            tvSpecies.text = cursor.getString(1)
            tvOwner.text = "Owner: ${cursor.getString(2)}"
        }

        cursor.close()
    }

    // =========================
    // LOAD MEDICATIONS
    // =========================
    private fun loadMedications(animalId: Int) {

        val list = ArrayList<Medication>()

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT med_id, animal_id, name, dosage, date, type
            FROM medications
            WHERE animal_id=?
            ORDER BY date DESC
            """.trimIndent(),
            arrayOf(animalId.toString())
        )

        while (cursor.moveToNext()) {
            list.add(
                Medication(
                    id = cursor.getInt(0),
                    name = cursor.getString(2),
                    dosage = cursor.getString(3),
                    date = cursor.getString(4),
                    type = cursor.getString(5)
                )
            )
        }

        cursor.close()

        adapter = MedAdapter(list, dbHelper) {
            loadMedications(animalId)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
}