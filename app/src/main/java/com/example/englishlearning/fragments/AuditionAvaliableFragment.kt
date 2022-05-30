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
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.Auditions
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.Provider


class AuditionAvaliableFragment : Fragment() {

    lateinit var recyclerViewAudAv : RecyclerView
    lateinit var progressBar : ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId : PaperUserId = Paper.book().read("userId")
        val id = userId.userId

        progressBar.visibility = View.VISIBLE

        val serviceBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuilder.getAudAv(id)
        requestCall.enqueue(object : Callback<ArrayList<Auditions>>{
            override fun onResponse(
                call: Call<ArrayList<Auditions>>,
                response: Response<ArrayList<Auditions>>
            ) {
                if (response.isSuccessful){
                    recyclerViewAudAv.layoutManager = LinearLayoutManager(context)
                    recyclerViewAudAv.setHasFixedSize(true)
                    recyclerViewAudAv.adapter = AuditionAvaliableAdapter(context!!, response.body()!!){
                        val action = AuditionFragmentDirections.actionAuditionFragmentToAuditionDetailFragment(it.id)
                        findNavController().navigate(action)
                    }
                    progressBar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<ArrayList<Auditions>>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Paper.init(context)
        val aaFrag =  inflater.inflate(R.layout.fragment_audition_avaliable, container, false)
        recyclerViewAudAv = aaFrag.findViewById(R.id.aud_av_list)
        progressBar = aaFrag.findViewById(R.id.pbAufAvLoading)
        return aaFrag
    }

    fun newInstance() = AuditionAvaliableFragment()
}