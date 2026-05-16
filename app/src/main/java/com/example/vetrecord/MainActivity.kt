package com.example.vetrecord

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: AnimalAdapter
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)



        dbHelper.insertAnimal(
            "Luna",
            "Cat",
            "Persian",
            3,
            "Sarah",
            "0555555555"
        )

        dbHelper.insertRecord(
            1,
            "Vaccination",
            "Rabies vaccine completed",
            "2026-05-16",
            "Healthy"
        )





        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        adapter = AnimalAdapter(arrayListOf())

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        // جلب البيانات من SQLite
        val allAnimals = dbHelper.getAllAnimals()

        adapter.updateList(allAnimals)

        // البحث
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    val result =
                        dbHelper.searchAnimals(newText ?: "")

                    adapter.updateList(result)

                    return true
                }
            }
        )
    }
}