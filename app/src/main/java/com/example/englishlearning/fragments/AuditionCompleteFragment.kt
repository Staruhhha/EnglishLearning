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
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.Auditions
import com.example.englishlearning.models.CompletedAuditions
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuditionCompleteFragment : Fragment() {

    lateinit var compAudView : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Paper.init(context)
        var id : PaperUserId = Paper.book().read("userId")

        val serviceBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuilder.getCompletedAud(id.userId)
        requestCall.enqueue(object : Callback<ArrayList<CompletedAuditions>>{
            override fun onResponse(
                call: Call<ArrayList<CompletedAuditions>>,
                response: Response<ArrayList<CompletedAuditions>>
            ) {
                if (response.isSuccessful){
                    compAudView.layoutManager = LinearLayoutManager(context)
                    compAudView.setHasFixedSize(true)
                    compAudView.adapter = AuditionCompletedAdapter(context!!, response.body()!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<CompletedAuditions>>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }


        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val audComp = inflater.inflate(R.layout.fragment_audition_complete, container, false)
        compAudView = audComp.findViewById(R.id.aud_comp)
        return audComp
    }

    fun newInstance() = AuditionCompleteFragment()
}