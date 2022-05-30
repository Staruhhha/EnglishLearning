package com.example.englishlearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearning.models.Auditions

class AuditionAvaliableAdapter (val context : Context,val listAuditions : ArrayList<Auditions>,
val listener: (Auditions) -> Unit) : RecyclerView.Adapter<AuditionAvaliableAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val auditionNumber = view.findViewById<TextView>(R.id.audition_number)
        val auditionLevel = view.findViewById<TextView>(R.id.audition_level)

        fun bindView(item : Auditions, listener: (Auditions) -> Unit){
            itemView.setOnClickListener { listener(item) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.audition_card, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.auditionNumber.text = "Задание №${listAuditions[position].id}"
        holder.auditionLevel.text = "Уровень: ${listAuditions[position].levelLanguageId}"

        holder.bindView(listAuditions[position], listener)
    }

    override fun getItemCount(): Int = listAuditions.size
}