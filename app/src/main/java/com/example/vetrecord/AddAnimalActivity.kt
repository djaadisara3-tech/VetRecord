package com.example.vetclinic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.ArrayAdapter
import android.widget.Spinner


import android.widget.Button
import android.widget.EditText


import android.widget.Toast
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {//دالة تفتح عندما نفتح الشاشة
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)//استعمل ملف xml هذا كواجهة للشاشة


        // إنشاء قاعدة البيانات

        val dbHelper = DBHelper(this)

        // ربط الحقول مع Kotlin

        val petName = findViewById<EditText>(R.id.etPetName)
        val petAge = findViewById<EditText>(R.id.etAge)
        val ownerName = findViewById<EditText>(R.id.etOwnerName)
        val phone = findViewById<EditText>(R.id.etPhone)
        val notes = findViewById<EditText>(R.id.etNotes)

        val saveButton = findViewById<Button>(R.id.btnSave)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)





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

        saveButton.setOnClickListener {


            // قراءة البيانات من الحقول

            val name = petName.text.toString()
            val age = petAge.text.toString()
            val owner = ownerName.text.toString()
            val phoneNumber = phone.text.toString()
            val breed = notes.text.toString()

            val animalType = spinnerType.selectedItem.toString()
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
