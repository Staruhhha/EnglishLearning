package com.example.englishlearning.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearning.AuditionAvaliableAdapter
import com.example.englishlearning.R
import com.example.englishlearning.ReadingAdapter
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.Readings
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReadingAvailableFragment : Fragment() {

    lateinit var recyclerView : RecyclerView
    lateinit var progressBar : ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId : PaperUserId = Paper.book().read("userId")
        val id = userId.userId

        progressBar.visibility = View.VISIBLE

        val serviceBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuilder.getReadingsAv(id)
        requestCall.enqueue(object : Callback<ArrayList<Readings>>{
            override fun onResponse(
                call: Call<ArrayList<Readings>>,
                response: Response<ArrayList<Readings>>
            ) {
                if (response.isSuccessful){
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter = ReadingAdapter(context!!, response.body()!!){
                        val action = ReadingFragmentDirections.actionReadingFragmentToReadingDetailFragment(it.id)
                        findNavController().navigate(action)
                    }
                    progressBar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<ArrayList<Readings>>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val readingAvFragment = inflater.inflate(R.layout.fragment_reading_available, container, false)
        Paper.init(context)
        recyclerView = readingAvFragment.findViewById(R.id.available_readings_list)
        progressBar = readingAvFragment.findViewById(R.id.reading_progress)
        return readingAvFragment
    }

    fun newInstance() = ReadingAvailableFragment()

}