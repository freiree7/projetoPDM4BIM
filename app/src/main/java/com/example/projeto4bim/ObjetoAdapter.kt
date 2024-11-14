package com.example.projeto4bim

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ObjetoAdapter(private var objetosList: List<Map<String, Any>>) :
    RecyclerView.Adapter<ObjetoAdapter.ObjetoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjetoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_objeto, parent, false)
        return ObjetoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ObjetoViewHolder, position: Int) {
        val objeto = objetosList[position]
        holder.nomeTextView.text = objeto["nome"] as? String
        holder.tipoTextView.text = objeto["tipo"] as? String
        holder.poderTextView.text = objeto["poder"] as? String
        holder.historiaTextView.text = objeto["historia"] as? String


        holder.botaoCompartilhar.setOnClickListener {
            val nome = objeto["nome"] as? String ?: ""
            val tipo = objeto["tipo"] as? String ?: ""
            val poder = objeto["poder"] as? String ?: ""
            val historia = objeto["historia"] as? String ?: ""

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Confira este objeto amaldiçoado:\n\n" +
                            "Nome: $nome\n" +
                            "Tipo: $tipo\n" +
                            "Poder: $poder\n" +
                            "História: $historia"
                )
            }
            holder.itemView.context.startActivity(Intent.createChooser(shareIntent, "Compartilhar objeto"))
        }

        val imagemBlob = objeto["imagem"] as? ByteArray
        if (imagemBlob != null) {
            val bitmap = BitmapFactory.decodeByteArray(imagemBlob, 0, imagemBlob.size)
            holder.imagemImageView.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount(): Int {
        return objetosList.size
    }

    class ObjetoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.objeto_nome)
        val tipoTextView: TextView = itemView.findViewById(R.id.objeto_tipo)
        val poderTextView: TextView = itemView.findViewById(R.id.objeto_poder)
        val historiaTextView: TextView = itemView.findViewById(R.id.objeto_historia)
        val imagemImageView: ImageView = itemView.findViewById(R.id.objeto_imagem)
        val botaoCompartilhar: Button = itemView.findViewById(R.id.botaoCompartilhar)
    }

    fun updateList(newList: List<Map<String, Any>>) {
        objetosList = newList
        notifyDataSetChanged()
    }
}
