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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
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
}