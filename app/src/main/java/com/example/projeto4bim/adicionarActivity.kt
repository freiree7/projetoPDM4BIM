package com.example.projeto4bim

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

class adicionarActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adicionar_objeto)

        dbHelper = DBHelper(this)

        val nomeEditText: EditText = findViewById(R.id.editTextText)
        val origemEditText: EditText = findViewById(R.id.editTextTextMultiLine)
        val tipoEditText: EditText = findViewById(R.id.editTextText2)
        val poderEditText: EditText = findViewById(R.id.editTextText3)

        val selecionarImagemButton: Button = findViewById(R.id.button)
        val enviarImagemButton: Button = findViewById(R.id.button2)
        val cadastrarButton: Button = findViewById(R.id.button3)

        selecionarImagemButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        cadastrarButton.setOnClickListener {
            val nome = nomeEditText.text.toString()
            val origem = origemEditText.text.toString()
            val tipo = tipoEditText.text.toString()
            val poder = poderEditText.text.toString()

            val imagemByteArray = selectedImageUri?.let { uri ->
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)?.let { bitmap ->
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        byteArrayOutputStream.toByteArray()
                    }
                }
            }

            if (imagemByteArray != null) {
                val result = dbHelper.addObjeto(nome, origem, tipo, poder, imagemByteArray)
                if (result != -1L) {
                    Toast.makeText(this, "Objeto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Falha no cadastro. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, selecione uma imagem.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            val imageStream = contentResolver.openInputStream(selectedImageUri!!)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
        }
    }
}
