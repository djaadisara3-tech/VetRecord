package com.example.vetrecord

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "vet_clinic.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

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

        db.execSQL("""
            CREATE TABLE records (
                record_id INTEGER PRIMARY KEY AUTOINCREMENT,
                animal_id INTEGER,
                type TEXT,
                description TEXT,
                date TEXT,
                extra TEXT
            );
        """)

        db.execSQL("""
            CREATE TABLE medications (
                med_id INTEGER PRIMARY KEY AUTOINCREMENT,
                animal_id INTEGER,
                name TEXT,
                dosage TEXT,
                date TEXT,
                type TEXT
            );
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS animals")
        db.execSQL("DROP TABLE IF EXISTS records")
        db.execSQL("DROP TABLE IF EXISTS medications")
        onCreate(db)
    }

    // ================= MEDICATION =================

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

    fun updateMedication(id: Int, name: String, dosage: String): Int {
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

    fun deleteMedication(id: Int): Int {
        val db = writableDatabase
        return db.delete("medications", "med_id=?", arrayOf(id.toString()))
    }

    // ================= ANIMAL =================

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

    fun deleteAnimal(id: Int): Int {
        val db = writableDatabase
        return db.delete("animals", "animal_id=?", arrayOf(id.toString()))
    }

    // ================= RECORDS =================

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

    fun getRecordsByAnimal(animalId: Int): ArrayList<String> {

        val list = ArrayList<String>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT type, description, date FROM records WHERE animal_id=?",
            arrayOf(animalId.toString())
        )

        while (cursor.moveToNext()) {
            list.add(
                "${cursor.getString(0)}\n${cursor.getString(1)}\n${cursor.getString(2)}"
            )
        }

        cursor.close()
        return list
    }

    // ================= SEARCH =================

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

        while (cursor.moveToNext()) {

            list.add(
                Animal(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    species = cursor.getString(2),
                    owner = cursor.getString(5)
                )
            )
        }

        cursor.close()
        return list
    }

    fun getAllAnimals(): ArrayList<Animal> {

        val list = ArrayList<Animal>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM animals", null)

        while (cursor.moveToNext()) {

            list.add(
                Animal(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    species = cursor.getString(2),
                    owner = cursor.getString(5)
                )
            )
        }

        //get recorde
        fun getRecordsByAnimalId(animalId: Int): ArrayList<Record> {

            val list = ArrayList<Record>()
            val db = readableDatabase

            val cursor = db.rawQuery(
                """
        SELECT type, description, date
        FROM records
        WHERE animal_id=?
        ORDER BY date DESC
        """.trimIndent(),
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
            return list
        }
        cursor.close()
        return list
    }
}