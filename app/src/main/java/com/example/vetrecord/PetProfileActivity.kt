package com.example.vetrecord

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PetProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_profile)

        // =========================
        // VIEWS
        // =========================
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvSpecies = findViewById<TextView>(R.id.tvSpecies)
        val tvOwner = findViewById<TextView>(R.id.tvOwner)

        val recordsContainer = findViewById<LinearLayout>(R.id.recordsContainer)
        val medContainer = findViewById<LinearLayout>(R.id.medContainer)

        // =========================
        // DATABASE
        // =========================
        val db = DBHelper(this).readableDatabase

        // =========================
        // GET ANIMAL ID
        // =========================
        val animalId = intent.getIntExtra("animal_id", -1)

        if (animalId == -1) {
            Toast.makeText(this, "Animal ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // =========================
        // ANIMAL INFO
        // =========================
        val animalCursor = db.rawQuery(
            "SELECT animal_name, species, owner_name FROM animals WHERE animal_id = ?",
            arrayOf(animalId.toString())
        )

        if (animalCursor.moveToFirst()) {
            tvName.text = animalCursor.getString(0)
            tvSpecies.text = animalCursor.getString(1)
            tvOwner.text = "Owner: ${animalCursor.getString(2)}"
        }
        animalCursor.close()

        // =========================
        // MEDICAL RECORDS
        // =========================
        recordsContainer.removeAllViews()

        val recordCursor = db.rawQuery(
            "SELECT type, description, date FROM records WHERE animal_id = ? ORDER BY date DESC",
            arrayOf(animalId.toString())
        )

        while (recordCursor.moveToNext()) {

            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(40, 30, 40, 30)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = 24
            card.layoutParams = params

            card.setBackgroundResource(R.drawable.white_card)

            val tvType = TextView(this)
            tvType.text = recordCursor.getString(0)
            tvType.setTypeface(null, android.graphics.Typeface.BOLD)
            tvType.textSize = 16f

            val tvDesc = TextView(this)
            tvDesc.text = recordCursor.getString(1)
            tvDesc.textSize = 14f

            val tvDate = TextView(this)
            tvDate.text = recordCursor.getString(2)
            tvDate.textSize = 12f

            card.addView(tvType)
            card.addView(tvDesc)
            card.addView(tvDate)

            recordsContainer.addView(card)
        }
        recordCursor.close()

        // =========================
        // MEDICATIONS + VACCINES
        // =========================
        medContainer.removeAllViews()

        val medCursor = db.rawQuery(
            "SELECT name, dosage, date, type FROM medications WHERE animal_id = ? ORDER BY date DESC",
            arrayOf(animalId.toString())
        )

        while (medCursor.moveToNext()) {

            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(40, 30, 40, 30)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = 24
            card.layoutParams = params

            card.setBackgroundResource(R.drawable.white_card)

            val tvTitle = TextView(this)
            tvTitle.text = "${medCursor.getString(3)}: ${medCursor.getString(0)}"
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD)
            tvTitle.textSize = 16f

            val tvDose = TextView(this)
            tvDose.text = "Dose: ${medCursor.getString(1)}"
            tvDose.textSize = 14f

            val tvDate = TextView(this)
            tvDate.text = medCursor.getString(2)
            tvDate.textSize = 12f

            card.addView(tvTitle)
            card.addView(tvDose)
            card.addView(tvDate)

            medContainer.addView(card)
        }

        medCursor.close()
    }
}