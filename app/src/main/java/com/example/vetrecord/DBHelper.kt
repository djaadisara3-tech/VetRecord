package com.example.vetrecord

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "vet_clinic.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        // جدول الحيوانات
        db.execSQL("""
            CREATE TABLE animals (
                animal_id INTEGER PRIMARY KEY AUTOINCREMENT,
                animal_name TEXT,
                species TEXT,
                breed TEXT,
                age INTEGER,
                owner_name TEXT,
                owner_phone TEXT
            );
        """)

        // جدول السجلات
        db.execSQL("""
            CREATE TABLE records (
                record_id INTEGER PRIMARY KEY AUTOINCREMENT,
                animal_id INTEGER,
                type TEXT,
                description TEXT,
                date TEXT,
                extra TEXT,
                FOREIGN KEY (animal_id) REFERENCES animals(animal_id)
            );
        """)
        db.execSQL("""
    CREATE TABLE medications (
        med_id INTEGER PRIMARY KEY AUTOINCREMENT,
        animal_id INTEGER,
        name TEXT,
        dosage TEXT,
        date TEXT,
        type TEXT,
        FOREIGN KEY (animal_id) REFERENCES animals(animal_id)
    );
""")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS records")
        db.execSQL("DROP TABLE IF EXISTS animals")
        onCreate(db)
    }


//دالة إدخال دواء / لقاح
    fun insertMedication(
        animalId: Int,
        name: String,
        dosage: String,
        date: String,
        type: String
    ) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("animal_id", animalId)
            put("name", name)
            put("dosage", dosage)
            put("date", date)
            put("type", type)
        }
        db.insert("medications", null, values)
    }

    // ---------------- INSERT ANIMAL ----------------
    fun insertAnimal(
        name: String,
        species: String,
        breed: String,
        age: Int,
        owner: String,
        phone: String
    ) {
        val db = writableDatabase
        val value = ContentValues().apply {
            put("animal_name", name)
            put("species", species)
            put("breed", breed)
            put("age", age)
            put("owner_name", owner)
            put("owner_phone", phone)
        }
        db.insert("animals", null, value)
    }

    // ---------------- INSERT RECORD ----------------
    fun insertRecord(
        animalId: Int,
        type: String,
        desc: String,
        date: String,
        extra: String
    ) {
        val db = writableDatabase
        val value = ContentValues().apply {
            put("animal_id", animalId)
            put("type", type)
            put("description", desc)
            put("date", date)
            put("extra", extra)
        }
        db.insert("records", null, value)
    }

    fun searchAnimals(keyword: String): ArrayList<Animal> {

        val list = ArrayList<Animal>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT * FROM animals
        WHERE animal_name LIKE ?
        OR owner_name LIKE ?
        """,
            arrayOf("%$keyword%", "%$keyword%")
        )

        if (cursor.moveToFirst()) {

            do {

                val animal = Animal(
                    id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("animal_id")
                    ),

                    name = cursor.getString(
                        cursor.getColumnIndexOrThrow("animal_name")
                    ),

                    species = cursor.getString(
                        cursor.getColumnIndexOrThrow("species")
                    ),

                    owner = cursor.getString(
                        cursor.getColumnIndexOrThrow("owner_name")
                    )
                )

                list.add(animal)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return list
    }

fun getAllAnimals(): ArrayList<Animal> {

    val list = ArrayList<Animal>()

    val db = readableDatabase

    val cursor = db.rawQuery(
        "SELECT * FROM animals",
        null
    )

    if (cursor.moveToFirst()) {

        do {

            val animal = Animal(
                id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("animal_id")
                ),

                name = cursor.getString(
                    cursor.getColumnIndexOrThrow("animal_name")
                ),

                species = cursor.getString(
                    cursor.getColumnIndexOrThrow("species")
                ),

                owner = cursor.getString(
                    cursor.getColumnIndexOrThrow("owner_name")
                )
            )

            list.add(animal)

        } while (cursor.moveToNext())
    }

    cursor.close()

    return list
}



    //لجلب السجلات الطبية الخاصة بحيوان معيّن
    fun getRecordsByAnimal(animalId: Int): ArrayList<String> {

        val list = ArrayList<String>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT * FROM records
        WHERE animal_id = ?
        """,
            arrayOf(animalId.toString())
        )

        if (cursor.moveToFirst()) {

            do {

                val type = cursor.getString(
                    cursor.getColumnIndexOrThrow("type")
                )

                val desc = cursor.getString(
                    cursor.getColumnIndexOrThrow("description")
                )

                val date = cursor.getString(
                    cursor.getColumnIndexOrThrow("date")
                )

                val record =
                    "$type\n$desc\n$date"

                list.add(record)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return list
    }

    // جلب Eye Treatment فقط
    fun getEyeTreatments(): ArrayList<String> {
        val list = ArrayList<String>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT type, description, date FROM records WHERE type = ?",
            arrayOf("Eye Treatment")
        )

        while (cursor.moveToNext()) {
            val type = cursor.getString(0)
            val desc = cursor.getString(1)
            val date = cursor.getString(2)

            list.add("$type\n$desc\n$date")
        }

        cursor.close()
        return list
    }
    fun deleteAnimal(animalId: Int): Int {

    val db = writableDatabase

    return db.delete(
        "animals",
        "animal_id=?",
        arrayOf(animalId.toString())
    )
}
    fun updateAnimal(
    animalId: Int,
    name: String,
    species: String,
    breed: String,
    age: Int,
    owner: String,
    phone: String
): Int {

    val db = writableDatabase

    val values = ContentValues().apply {

        put("animal_name", name)
        put("species", species)
        put("breed", breed)
        put("age", age)
        put("owner_name", owner)
        put("owner_phone", phone)
    }

    return db.update(
        "animals",
        values,
        "animal_id=?",
        arrayOf(animalId.toString())
    )
}
}
