package com.example.vetrecord

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddAnimalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_animal)

        // ================= DATABASE =================
        val dbHelper = DBHelper(this)

        // ================= INPUTS =================
        val petName = findViewById<EditText>(R.id.etPetName)
        val petAge = findViewById<EditText>(R.id.etAge)
        val ownerName = findViewById<EditText>(R.id.etOwnerName)
        val phone = findViewById<EditText>(R.id.etPhone)
        val breed = findViewById<EditText>(R.id.etNotes)

        // ================= BUTTON =================
        val saveButton = findViewById<Button>(R.id.btnSave)
        val backButton = findViewById<ImageButton>(R.id.btnBack)

        // ================= SPINNER =================
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)

        val petTypes = arrayOf("Dog", "Cat", "Bird", "Rabbit")

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            petTypes
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerType.adapter = adapter

        // ================= BACK BUTTON =================
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

       //edit
        val intent = intent

        val id = intent.getIntExtra("id", -1)
        val oldName = intent.getStringExtra("name")
        val oldSpecies = intent.getStringExtra("species")
        val oldOwner = intent.getStringExtra("owner")


        // ================= SAVE =================
        saveButton.setOnClickListener {

            val name = petName.text.toString().trim()
            val age = petAge.text.toString().trim()
            val owner = ownerName.text.toString().trim()
            val phoneNumber = phone.text.toString().trim()
            val breedText = breed.text.toString().trim()
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

            Toast.makeText(this, "Animal saved successfully!", Toast.LENGTH_SHORT).show()

            // clear
            petName.text.clear()
            petAge.text.clear()
            ownerName.text.clear()
            phone.text.clear()
            breed.text.clear()
            spinnerType.setSelection(0)
        }
    }
}