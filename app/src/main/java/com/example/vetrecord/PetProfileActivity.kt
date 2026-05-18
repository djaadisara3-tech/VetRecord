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

    lateinit var medRecycler: RecyclerView
    lateinit var recordRecycler: RecyclerView

    lateinit var medAdapter: MedAdapter
    lateinit var recordAdapter: RecordAdapter

    lateinit var dbHelper: DBHelper
    lateinit var btnAddMed: FloatingActionButton

    var animalId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_profile)

        // ================= INIT VIEWS =================
        tvName = findViewById(R.id.tvName)
        tvSpecies = findViewById(R.id.tvSpecies)
        tvOwner = findViewById(R.id.tvOwner)

        medRecycler = findViewById(R.id.medRecycler)
        recordRecycler = findViewById(R.id.recordRecycler)

        btnAddMed = findViewById(R.id.btnAddMed)

        dbHelper = DBHelper(this)

        animalId = intent.getIntExtra("animal_id", -1)

        if (animalId == -1) {
            Toast.makeText(this, "Animal ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ================= LOAD DATA =================
        loadAnimalInfo()
        loadMedications(animalId)
        loadRecords(animalId)

        // ================= ADD MEDICATION =================
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
                .setTitle("Add Medication")
                .setView(layout)
                .setPositiveButton("Save") { _, _ ->

                    dbHelper.insertMedication(
                        animalId,
                        inputName.text.toString(),
                        inputDose.text.toString(),
                        inputDate.text.toString(),
                        "Medicine"
                    )

                    loadMedications(animalId)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    // ================= ANIMAL INFO =================
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

    // ================= MEDICATIONS =================
    private fun loadMedications(animalId: Int) {

        val list = ArrayList<Medication>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT med_id, name, dosage, date, type
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
                    name = cursor.getString(1),
                    dosage = cursor.getString(2),
                    date = cursor.getString(3),
                    type = cursor.getString(4)
                )
            )
        }

        cursor.close()

        medAdapter = MedAdapter(list, dbHelper) {
            loadMedications(animalId)
        }

        medRecycler.layoutManager = LinearLayoutManager(this)
        medRecycler.adapter = medAdapter
    }

    // ================= RECORDS =================
    private fun loadRecords(animalId: Int) {

        val list = ArrayList<Record>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT type, description, date FROM records WHERE animal_id=?",
            arrayOf(animalId.toString())
        )

        while (cursor.moveToNext()) {
            list.add(
                Record(
                    type = cursor.getString(0),
                    description = cursor.getString(1),
                    date = cursor.getString(2)
                )
            )
        }

        cursor.close()

        recordAdapter = RecordAdapter(list)

        recordRecycler.layoutManager = LinearLayoutManager(this)
        recordRecycler.adapter = recordAdapter
    }
}