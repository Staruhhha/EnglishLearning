package com.example.englishlearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearning.models.Auditions
import com.example.englishlearning.models.CompletedAuditions

class AuditionCompletedAdapter(val context: Context, val listCompleted : ArrayList<CompletedAuditions>) : RecyclerView.Adapter<AuditionCompletedAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val auditionNumber = view.findViewById<TextView>(R.id.audition_number)
        val auditionLevel = view.findViewById<TextView>(R.id.audition_level)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.audition_card, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.auditionNumber.text = "Задание №${listCompleted[position].audition.id}"
        holder.auditionLevel.text = "Уровень: ${listCompleted[position].audition.levelLanguageId}"
    }

    override fun getItemCount(): Int = listCompleted.size
}