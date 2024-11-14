package com.example.projeto4bim

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, "objetos.db", null, 2) { // Versão 2 do banco

    override fun onCreate(db: SQLiteDatabase?) {

        val createTableQuery = """
            CREATE TABLE objetosmaldicao (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                historia TEXT,
                tipo TEXT,
                poder TEXT,
                imagem BLOB
            );
        """.trimIndent()

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        if (oldVersion < newVersion) {

            db?.execSQL("ALTER TABLE objetosmaldicao ADD COLUMN historia TEXT")
        }
    }


    fun addObjeto(nome: String, historia: String, tipo: String, poder: String, imagem: ByteArray): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("nome", nome)
        contentValues.put("historia", historia) // Agora inserindo a história
        contentValues.put("tipo", tipo)
        contentValues.put("poder", poder)
        contentValues.put("imagem", imagem)

        val result = db.insert("objetosmaldicao", null, contentValues)
        db.close()
        return result
    }


    fun getAllObjetos(): List<Map<String, Any>> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM objetosmaldicao", null)

        val objetosList = mutableListOf<Map<String, Any>>()


        val columnNames = cursor.columnNames
        Log.d("DBHelper", "Colunas disponíveis: ${columnNames.joinToString(", ")}")

        if (cursor.moveToFirst()) {
            do {
                val objeto = mutableMapOf<String, Any>()
                try {
                    // Verifique se a coluna existe antes de acessar
                    val idIndex = cursor.getColumnIndex("id")
                    val nomeIndex = cursor.getColumnIndex("nome")
                    val historiaIndex = cursor.getColumnIndex("historia")
                    val tipoIndex = cursor.getColumnIndex("tipo")
                    val poderIndex = cursor.getColumnIndex("poder")
                    val imagemIndex = cursor.getColumnIndex("imagem")


                    if (idIndex == -1 || nomeIndex == -1 || historiaIndex == -1 || tipoIndex == -1 || poderIndex == -1 || imagemIndex == -1) {
                        Log.e("DBHelper", "Uma ou mais colunas não foram encontradas!")
                    }


                    objeto["id"] = cursor.getInt(idIndex)
                    objeto["nome"] = cursor.getString(nomeIndex)
                    objeto["historia"] = cursor.getString(historiaIndex)
                    objeto["tipo"] = cursor.getString(tipoIndex)
                    objeto["poder"] = cursor.getString(poderIndex)
                    objeto["imagem"] = cursor.getBlob(imagemIndex)

                    objetosList.add(objeto)
                } catch (e: Exception) {
                    Log.e("DBHelper", "Erro ao acessar dados: ${e.message}")
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return objetosList
    }
}
