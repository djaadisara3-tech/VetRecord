package com.example.vetrecord

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import android.content.Intent

class AddAnimalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_animal)

        val dbHelper = DBHelper(this)

        val petName = findViewById<EditText>(R.id.etPetName)
        val petAge = findViewById<EditText>(R.id.etAge)
        val ownerName = findViewById<EditText>(R.id.etOwnerName)
        val phone = findViewById<EditText>(R.id.etPhone)
        val breed = findViewById<EditText>(R.id.etNotes)
        val animalId = findViewById<EditText>(R.id.etAnimalId)

        val saveButton = findViewById<Button>(R.id.btnSave)
        val deleteButton = findViewById<Button>(R.id.btnDelete)
        val updateButton = findViewById<Button>(R.id.btnUpdate)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val backButton = findViewById<ImageButton>(R.id.btnBack)

        val petTypes = arrayOf("Dog", "Cat", "Bird", "Rabbit")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            petTypes
        )

        spinnerType.adapter = adapter

        backButton.setOnClickListener {
            startActivity(Intent(this, PetProfileActivity::class.java))
            finish()
        }

        // ================= DELETE =================
        deleteButton.setOnClickListener {

            val idText = animalId.text.toString()

            if (idText.isEmpty()) {
                Toast.makeText(this, "Enter Animal ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.deleteAnimal(idText.toInt())

            Toast.makeText(
                this,
                if (result > 0) "Deleted successfully" else "Not found",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ================= UPDATE =================
        updateButton.setOnClickListener {

            val idText = animalId.text.toString()

            if (idText.isEmpty()) {
                Toast.makeText(this, "Enter Animal ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.updateAnimal(
                idText.toInt(),
                petName.text.toString(),
                spinnerType.selectedItem.toString(), // species
                breed.text.toString(),               // breed
                petAge.text.toString().toInt(),
                ownerName.text.toString(),
                phone.text.toString()
            )

            Toast.makeText(
                this,
                if (result > 0) "Updated successfully" else "Update failed",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ================= SAVE =================
        saveButton.setOnClickListener {

            val name = petName.text.toString()
            val age = petAge.text.toString()
            val owner = ownerName.text.toString()
            val phoneNumber = phone.text.toString()
            val breedText = breed.text.toString()
            val type = spinnerType.selectedItem.toString()

            if (name.isEmpty() || age.isEmpty() || owner.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dbHelper.insertAnimal(
                name,
                type,
                breedText,
                age.toInt(),
                owner,
                phoneNumber
            )

            Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}