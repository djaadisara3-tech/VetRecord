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
            )
        """)

        // جدول السجلات الطبية
        db.execSQL("""
            CREATE TABLE records (
                record_id INTEGER PRIMARY KEY AUTOINCREMENT,
                animal_id INTEGER,
                type TEXT,
                description TEXT,
                date TEXT,
                extra TEXT,
                FOREIGN KEY (animal_id)
                REFERENCES animals(animal_id)
            )
        """)

        // جدول الأدوية واللقاحات
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

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS medications")
        db.execSQL("DROP TABLE IF EXISTS records")
        db.execSQL("DROP TABLE IF EXISTS animals")

        onCreate(db)
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

        val values = ContentValues().apply {

            put("animal_name", name)
            put("species", species)
            put("breed", breed)
            put("age", age)
            put("owner_name", owner)
            put("owner_phone", phone)
        }

        db.insert("animals", null, values)

        db.close()
    }

    // ---------------- GET ALL ANIMALS ----------------

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
        db.close()

        return list
    }

    // ---------------- SEARCH ANIMALS ----------------

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
        db.close()

        return list
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

        val values = ContentValues().apply {

            put("animal_id", animalId)
            put("type", type)
            put("description", desc)
            put("date", date)
            put("extra", extra)
        }

        db.insert("records", null, values)

        db.close()
    }

    // ---------------- GET RECORDS ----------------

    fun getRecordsByAnimal(
        animalId: Int
    ): ArrayList<String> {

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

                list.add(
                    "$type\n$desc\n$date"
                )

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }

    // ---------------- INSERT MEDICATION ----------------

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

        db.close()
    }


//دوال Edit + Delete: الخاصة بلقحات والادوية
    fun getMedicationsByAnimal(animalId: Int): ArrayList<Medication> {

        val list = ArrayList<Medication>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT med_id, name, dosage, date, type FROM medications WHERE animal_id = ? ORDER BY date DESC",
            arrayOf(animalId.toString())
        )

        while (cursor.moveToNext()) {

            list.add(
                Medication(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    dosage = cursor.getString(2),
                    date = cursor.getString(3),
                    type = cursor.getString(4)
                )
            )
        }

        cursor.close()
        return list
    }

    fun deleteMedication(id: Int) {
        val db = writableDatabase
        db.delete("medications", "med_id=?", arrayOf(id.toString()))
    }

    fun updateMedication(id: Int, name: String, dose: String) {
        val db = writableDatabase

        db.execSQL(
            "UPDATE medications SET name=?, dosage=? WHERE med_id=?",
            arrayOf(name, dose, id.toString())
        )
    }
}