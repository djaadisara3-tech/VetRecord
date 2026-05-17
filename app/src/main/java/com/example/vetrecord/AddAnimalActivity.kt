package com.example.vetrecord

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.ArrayAdapter
import android.widget.Spinner


import android.widget.Button
import android.widget.EditText


import android.widget.ImageButton

import android.content.Intent

import android.widget.Toast
class AddAnimalActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {//دالة تفتح عندما نفتح الشاشة
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_add_animal)//استعمل ملف xml هذا كواجهة للشاشة


        // إنشاء قاعدة البيانات

        val dbHelper = DBHelper(this)

        // ربط الحقول مع Kotlin

        val petName = findViewById<EditText>(R.id.etPetName)
        val petAge = findViewById<EditText>(R.id.etAge)
        val ownerName = findViewById<EditText>(R.id.etOwnerName)
        val phone = findViewById<EditText>(R.id.etPhone)
        val notes = findViewById<EditText>(R.id.etNotes)
        val animalId = findViewById<EditText>(R.id.etAnimalId)

        val saveButton = findViewById<Button>(R.id.btnSave)
        val deleteButton = findViewById<Button>(R.id.btnDelete)
        val updateButton = findViewById<Button>(R.id.btnUpdate)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)

        val backButton = findViewById<ImageButton>(R.id.btnBack)





        // قائمة أنواع الحيوانات
        val petTypes = arrayOf("Dog", "Cat", "Bird", "Rabbit")

// ربط الـ Spinner


// Adapter لعرض البيانات داخل Spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            petTypes
        )

        spinnerType.adapter = adapter
        // عند الضغط على زر الحفظ




backButton.setOnClickListener {

    val intent = Intent(
        this,
        PetProfileActivity::class.java
    )

    startActivity(intent)

    finish()
}





        deleteButton.setOnClickListener {

   val idText = animalId.text.toString()

    if (idText.isEmpty()) {

        Toast.makeText(
            this,
            "Enter Animal ID",
            Toast.LENGTH_SHORT
        ).show()

    } else {

        val result = dbHelper.deleteAnimal(idText.toInt())

        if (result > 0) {

            Toast.makeText(
                this,
                "Pet deleted successfully",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            Toast.makeText(
                this,
                "Animal not found",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


        updateButton.setOnClickListener {

    val idText = animalId.text.toString()

    if (idText.isEmpty()) {

        Toast.makeText(
            this,
            "Enter Animal ID",
            Toast.LENGTH_SHORT
        ).show()

    } else {

        val result = dbHelper.updateAnimal(

            idText.toInt(),

            petName.text.toString(),

            spinnerType.selectedItem.toString(),

            notes.text.toString(),

            petAge.text.toString().toInt(),

            ownerName.text.toString(),

            phone.text.toString()
        )

        if (result > 0) {

            Toast.makeText(
                this,
                "Pet updated successfully",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            Toast.makeText(
                this,
                "Update failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
        saveButton.setOnClickListener {


            // قراءة البيانات من الحقول

            val name = petName.text.toString()
            val age = petAge.text.toString()
            val owner = ownerName.text.toString()
            val phoneNumber = phone.text.toString()
            val breed = notes.text.toString()

            val animalType = spinnerType.selectedItem.toString()
            // التحقق إذا كانت الحقول فارغة

          // التحقق إذا كانت الحقول فارغة

if (
    name.isEmpty() ||
    age.isEmpty() ||
    owner.isEmpty() ||
    phoneNumber.isEmpty()
) {

    Toast.makeText(
        this,
        "Please fill all fields",
        Toast.LENGTH_SHORT
    ).show()

} else {

    val ageNumber = age.toInt()

    // التحقق من العمر

    if (ageNumber <= 0 || ageNumber > 30) {

        Toast.makeText(
            this,
            "Please enter a valid age",
            Toast.LENGTH_SHORT
        ).show()

    } else {

        // حفظ البيانات داخل قاعدة البيانات

        dbHelper.insertAnimal(
            name,
            animalType,
            breed,
            age.toInt(),
            owner,
            phoneNumber
        )

        Toast.makeText(
            this,
            "Pet saved successfully!",
            Toast.LENGTH_SHORT
        ).show()
    }
}
}
}
     }
