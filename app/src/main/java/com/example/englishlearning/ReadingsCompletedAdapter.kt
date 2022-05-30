package com.example.englishlearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearning.models.CompletedReadings

class ReadingsCompletedAdapter(val context: Context, val comReadings : ArrayList<CompletedReadings>) : RecyclerView.Adapter<ReadingsCompletedAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val comNumber = view.findViewById<TextView>(R.id.audition_number)
        val comLevel = view.findViewById<TextView>(R.id.audition_level)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.audition_card, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.comNumber.text = "Задание № ${comReadings[position].reading!!.id}"
        holder.comLevel.text = "Уровень: ${comReadings[position].reading!!.levelLanguageId}"
    }

    override fun getItemCount(): Int = comReadings.size
}