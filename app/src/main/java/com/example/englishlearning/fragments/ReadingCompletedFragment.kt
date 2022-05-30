package com.example.englishlearning.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearning.AuditionCompletedAdapter
import com.example.englishlearning.R
import com.example.englishlearning.ReadingsCompletedAdapter
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.CompletedReadings
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReadingCompletedFragment : Fragment() {

    lateinit var recyclerComReading : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId : PaperUserId = Paper.book().read("userId")

        val serviceBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val request = serviceBuilder.getCompletedReading(userId.userId)
        request.enqueue(object : Callback<ArrayList<CompletedReadings>>{
            override fun onResponse(
                call: Call<ArrayList<CompletedReadings>>,
                response: Response<ArrayList<CompletedReadings>>
            ) {
                if (response.isSuccessful){
                    recyclerComReading.layoutManager = LinearLayoutManager(context)
                    recyclerComReading.setHasFixedSize(true)
                    recyclerComReading.adapter = ReadingsCompletedAdapter(context!!, response.body()!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<CompletedReadings>>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val comReadingFrag = inflater.inflate(R.layout.fragment_reading_completed, container, false)
        Paper.init(context)
        recyclerComReading = comReadingFrag.findViewById(R.id.completed_readings_list)
        return comReadingFrag
    }

    fun newInstance() = ReadingCompletedFragment()

}