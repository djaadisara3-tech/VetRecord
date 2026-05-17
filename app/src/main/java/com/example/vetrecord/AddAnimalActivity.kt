package com.example.vetrecord

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*

class AddAnimalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ هنا واجهة الإضافة
        setContentView(R.layout.activity_add_animal)

        val dbHelper = DBHelper(this)

        val petName = findViewById<EditText>(R.id.etPetName)
        val petAge = findViewById<EditText>(R.id.etAge)
        val ownerName = findViewById<EditText>(R.id.etOwnerName)
        val phone = findViewById<EditText>(R.id.etPhone)
        val notes = findViewById<EditText>(R.id.etNotes)

        val saveButton = findViewById<Button>(R.id.btnSave)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)

        val petTypes = arrayOf("Dog", "Cat", "Bird", "Rabbit")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            petTypes
        )

        spinnerType.adapter = adapter

        saveButton.setOnClickListener {

            val name = petName.text.toString()
            val age = petAge.text.toString()
            val owner = ownerName.text.toString()
            val phoneNumber = phone.text.toString()
            val breed = notes.text.toString()
            val animalType = spinnerType.selectedItem.toString()

            if (
                name.isEmpty() ||
                age.isEmpty() ||
                owner.isEmpty() ||
                phoneNumber.isEmpty()
            ) {

                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()

            } else {

                val ageNumber = age.toInt()

                if (ageNumber <= 0 || ageNumber > 30) {

                    Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()

                } else {

                    dbHelper.insertAnimal(
                        name,
                        animalType,
                        breed,
                        ageNumber,
                        owner,
                        phoneNumber
                    )

                    Toast.makeText(this, "Pet saved successfully!", Toast.LENGTH_SHORT).show()

                    // 🔥 يرجع للشاشة الرئيسية
                    finish()
                }
            }
        }
    }
}