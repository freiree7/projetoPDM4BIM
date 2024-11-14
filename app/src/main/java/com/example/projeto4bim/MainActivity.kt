package com.example.projeto4bim

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.MutableLiveData
import com.example.projeto4bim.DBHelper
import com.example.projeto4bim.R

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: ObjetoAdapter
    private lateinit var objetosList: List<Map<String, Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
       objetosList = dbHelper.getAllObjetos()


        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewObjetos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ObjetoAdapter(objetosList)
        recyclerView.adapter = adapter


        val botaoAdd = findViewById<Button>(R.id.botao_add)
        botaoAdd.setOnClickListener {
            val intent = Intent(this, adicionarActivity::class.java)
            startActivity(intent)
        }


        val searchView: SearchView = findViewById(R.id.searchView)
        val searchViewTipo: SearchView = findViewById(R.id.searchViewTipo)


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        searchViewTipo.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = if (newText.isNullOrEmpty()) {
                    objetosList
                } else {
                    objetosList.filter {
                        it["tipo"].toString().contains(newText, ignoreCase = true)
                    }
                }
                adapter.updateList(filteredList)
                return true
            }
        })
    }


    private fun filterList(query: String?) {
        val filteredList = objetosList.filter {
            val nome = it["nome"]?.toString() ?: ""
            nome.contains(query ?: "", ignoreCase = true)
        }
        adapter.updateList(filteredList)
    }

    override fun onResume() {
        super.onResume()
        objetosList = dbHelper.getAllObjetos()
        adapter.updateList(objetosList)
    }
}

