package com.example.vetrecord

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // =========================================
    // VARIABLES
    // =========================================

    lateinit var dbHelper: DBHelper

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: AnimalAdapter

    lateinit var searchView: SearchView

    // =========================================
    // ON CREATE
    // =========================================

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // =====================================
        // DATABASE
        // =====================================

        dbHelper = DBHelper(this)

        // =====================================
        // VIEWS
        // =====================================

        recyclerView =
            findViewById(R.id.recyclerView)

        searchView =
            findViewById(R.id.searchView)

        val fab =
            findViewById<FloatingActionButton>(R.id.fab)

        // =====================================
        // ADAPTER
        // =====================================

        adapter = AnimalAdapter(arrayListOf())

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        // =====================================
        // LOAD ALL ANIMALS
        // =====================================

        loadAnimals()

        // =====================================
        // FAB CLICK -> OPEN ADD SCREEN
        // =====================================

        fab.setOnClickListener {

            val intent = Intent(
                this,
                AddAnimalActivity::class.java
            )

            startActivity(intent)
        }

        // =====================================
        // SEARCH VIEW
        // =====================================

        searchView.setOnQueryTextListener(

            object : SearchView.OnQueryTextListener {

                // SEARCH BUTTON CLICK
                override fun onQueryTextSubmit(
                    query: String?
                ): Boolean {

                    return false
                }

                // TEXT CHANGED
                override fun onQueryTextChange(
                    newText: String?
                ): Boolean {

                    val result = if (
                        newText.isNullOrEmpty()
                    ) {

                        dbHelper.getAllAnimals()

                    } else {

                        dbHelper.searchAnimals(
                            newText
                        )
                    }

                    // UPDATE LIST
                    adapter.updateList(result)

                    return true
                }
            }
        )
    }

    // =========================================
    // LOAD ANIMALS FROM DATABASE
    // =========================================

    private fun loadAnimals() {

        val allAnimals =
            dbHelper.getAllAnimals()

        adapter.updateList(allAnimals)
    }

    // =========================================
    // REFRESH AFTER RETURNING
    // =========================================

    override fun onResume() {

        super.onResume()

        loadAnimals()
    }
}