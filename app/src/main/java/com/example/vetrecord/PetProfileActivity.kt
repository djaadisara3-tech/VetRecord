package com.example.vetrecord

import android.content.Intent
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
    lateinit var btnAdd: FloatingActionButton

    var animalId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_profile)

        // ================= ACTION BAR BACK =================
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // ================= INIT =================
        tvName = findViewById(R.id.tvName)
        tvSpecies = findViewById(R.id.tvSpecies)
        tvOwner = findViewById(R.id.tvOwner)

        medRecycler = findViewById(R.id.medRecycler)
        recordRecycler = findViewById(R.id.recordRecycler)

        btnAdd = findViewById(R.id.btnAddMed)

        dbHelper = DBHelper(this)

        animalId = intent.getIntExtra("animal_id", -1)

        if (animalId == -1) {
            Toast.makeText(this, "Animal ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadAnimalInfo()
        loadMedications(animalId)
        loadRecords(animalId)

        btnAdd.setOnClickListener {

            val options = arrayOf("Add Medication", "Add Medical Record")

            AlertDialog.Builder(this)
                .setTitle("Choose Action")
                .setItems(options) { _, which ->

                    when (which) {

                        0 -> addMedicationDialog()
                        1 -> addRecordDialog()
                    }
                }
                .show()
        }
    }

    // ================= BACK BUTTON FIX =================
    override fun onSupportNavigateUp(): Boolean {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

        return true
    }

    // ================= MEDICATION DIALOG =================
    private fun addMedicationDialog() {

        val name = EditText(this).apply { hint = "Medicine Name" }
        val dose = EditText(this).apply { hint = "Dosage 5ml" }
        val date = EditText(this).apply { hint = "Date 2026-05-18" }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 10)

            addView(name)
            addView(dose)
            addView(date)
        }

        AlertDialog.Builder(this)
            .setTitle("Add Medication")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->

                dbHelper.insertMedication(
                    animalId,
                    name.text.toString(),
                    dose.text.toString(),
                    date.text.toString(),
                    "Medicine"
                )

                loadMedications(animalId)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // ================= RECORD DIALOG =================
    private fun addRecordDialog() {

        val type = EditText(this).apply { hint = "Type (Checkup / Surgery)" }
        val desc = EditText(this).apply { hint = "Description" }
        val date = EditText(this).apply { hint = "Date" }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 10)

            addView(type)
            addView(desc)
            addView(date)
        }

        AlertDialog.Builder(this)
            .setTitle("Add Medical Record")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->

                dbHelper.insertRecord(
                    animalId,
                    type.text.toString(),
                    desc.text.toString(),
                    date.text.toString(),
                    ""
                )

                loadRecords(animalId)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // ================= LOAD ANIMAL =================
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
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
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

        val list = dbHelper.getRecordsByAnimalId(animalId)

        recordAdapter = RecordAdapter(list, dbHelper) {
            loadRecords(animalId)
        }

        recordRecycler.layoutManager = LinearLayoutManager(this)
        recordRecycler.adapter = recordAdapter
    }
}