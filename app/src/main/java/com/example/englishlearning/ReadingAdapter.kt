package com.example.englishlearning

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearning.models.Readings

class ReadingAdapter(val context: Context, val reading : ArrayList<Readings>,
val listener : (Readings) -> Unit) : RecyclerView.Adapter<ReadingAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view)  {
        val numberTask = view.findViewById<TextView>(R.id.audition_number)
        val levelTask = view.findViewById<TextView>(R.id.audition_level)

        fun bindView(item : Readings, listener: (Readings) -> Unit){
            itemView.setOnClickListener { listener(item) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.audition_card, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.numberTask.text = "Задание №${reading[position].id}"
        holder.levelTask.text = "Уровень: ${reading[position].levelLanguageId}"
        holder.bindView(reading[position], listener)
    }

    override fun getItemCount(): Int = reading.size
}