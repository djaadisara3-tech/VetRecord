package com.example.vetrecord

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "vet_clinic.db", null, 2){

    // =================================================
    // CREATE DATABASE
    // =================================================
    override fun onCreate(db: SQLiteDatabase) {

        // ================= ANIMALS =================
        db.execSQL(
            """
            CREATE TABLE animals (
                animal_id INTEGER PRIMARY KEY AUTOINCREMENT,
                animal_name TEXT,
                species TEXT,
                breed TEXT,
                age INTEGER,
                owner_name TEXT,
                owner_phone TEXT
            );
            """.trimIndent()
        )

        // ================= RECORDS =================
        db.execSQL(
            """
            CREATE TABLE records (
                record_id INTEGER PRIMARY KEY AUTOINCREMENT,
                animal_id INTEGER,
                type TEXT,
                description TEXT,
                date TEXT,
                extra TEXT
            );
            """.trimIndent()
        )

        // ================= MEDICATIONS =================
        db.execSQL(
            """
            CREATE TABLE medications (
                med_id INTEGER PRIMARY KEY AUTOINCREMENT,
                animal_id INTEGER,
                name TEXT,
                dosage TEXT,
                date TEXT,
                type TEXT
            );
            """.trimIndent()
        )
    }

    // =================================================
    // UPGRADE DATABASE
    // =================================================
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS animals")
        db.execSQL("DROP TABLE IF EXISTS records")
        db.execSQL("DROP TABLE IF EXISTS medications")

        onCreate(db)
    }

    // =================================================
    // INSERT ANIMAL
    // =================================================
    fun insertAnimal(
        name: String,
        species: String,
        breed: String,
        age: Int,
        owner: String,
        phone: String
    ) {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("animal_name", name)
            put("species", species)
            put("breed", breed)
            put("age", age)
            put("owner_name", owner)
            put("owner_phone", phone)
        }

        db.insert("animals", null, values)
    }

    // =================================================
    // UPDATE ANIMAL
    // =================================================
    fun updateAnimal(
        id: Int,
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
            arrayOf(id.toString())
        )
    }

    // =================================================
    // DELETE ANIMAL
    // =================================================
    fun deleteAnimal(id: Int): Int {

        val db = writableDatabase

        return db.delete(
            "animals",
            "animal_id=?",
            arrayOf(id.toString())
        )
    }

    // =================================================
    // INSERT RECORD
    // =================================================
    fun insertRecord(
        animalId: Int,
        type: String,
        desc: String,
        date: String,
        extra: String
    ) {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("animal_id", animalId)
            put("type", type)
            put("description", desc)
            put("date", date)
            put("extra", extra)
        }

        db.insert("records", null, values)
    }

    // =================================================
    // GET RECORDS
    // =================================================
    fun getRecordsByAnimalId(animalId: Int): ArrayList<Record> {

        val list = ArrayList<Record>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT record_id, type, description, date
            FROM records
            WHERE animal_id=?
            ORDER BY date DESC
            """.trimIndent(),
            arrayOf(animalId.toString())
        )

        while (cursor.moveToNext()) {

            list.add(
                Record(
                    id = cursor.getInt(0),
                    type = cursor.getString(1),
                    description = cursor.getString(2),
                    date = cursor.getString(3)
                )
            )
        }

        cursor.close()
        return list
    }

    // =================================================
    // UPDATE RECORD
    // =================================================
    fun updateRecord(
        id: Int,
        type: String,
        description: String,
        date: String
    ): Int {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("type", type)
            put("description", description)
            put("date", date)
        }

        return db.update(
            "records",
            values,
            "record_id=?",
            arrayOf(id.toString())
        )
    }

    // =================================================
    // DELETE RECORD
    // =================================================
    fun deleteRecord(id: Int): Int {

        val db = writableDatabase

        return db.delete(
            "records",
            "record_id=?",
            arrayOf(id.toString())
        )
    }

    // =================================================
    // INSERT MEDICATION
    // =================================================
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

    // =================================================
    // UPDATE MEDICATION
    // =================================================
    fun updateMedication(
        id: Int,
        name: String,
        dosage: String
    ): Int {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("name", name)
            put("dosage", dosage)
        }

        return db.update(
            "medications",
            values,
            "med_id=?",
            arrayOf(id.toString())
        )
    }

    // =================================================
    // DELETE MEDICATION
    // =================================================
    fun deleteMedication(id: Int): Int {

        val db = writableDatabase

        return db.delete(
            "medications",
            "med_id=?",
            arrayOf(id.toString())
        )
    }

    // =================================================
    // GET ALL ANIMALS
    // =================================================
    fun getAllAnimals(): ArrayList<Animal> {

        val list = ArrayList<Animal>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM animals",
            null
        )

        while (cursor.moveToNext()) {

            list.add(
                Animal(
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
            )
        }

        cursor.close()
        return list
    }

    // =================================================
    // SEARCH ANIMALS
    // =================================================
    fun searchAnimals(keyword: String): ArrayList<Animal> {

        val list = ArrayList<Animal>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT * FROM animals
            WHERE animal_name LIKE ?
            OR owner_name LIKE ?
            """.trimIndent(),
            arrayOf("%$keyword%", "%$keyword%")
        )

        while (cursor.moveToNext()) {

            list.add(
                Animal(
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
            )
        }

        cursor.close()
        return list
    }
}