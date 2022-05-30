package com.example.englishlearning.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.englishlearning.R
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.Users
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private lateinit var profilePg : ProgressBar
    private lateinit var nameProfile : EditText
    private lateinit var surnameProfile : EditText
    private lateinit var emailProfile : EditText
    private lateinit var passwordProfile : EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId : PaperUserId = Paper.book().read("userId")
        val serviceBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuilder.getUserById(userId.userId)
        requestCall.enqueue(object : Callback<Users>{
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful){
                    nameProfile.setText(response.body()!!.name)
                    surnameProfile.setText(response.body()!!.surname)
                    emailProfile.setText(response.body()!!.login)
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentProfile = inflater.inflate(R.layout.fragment_profile, container, false)
        Paper.init(context)
        profilePg = fragmentProfile.findViewById(R.id.profile_loading)
        nameProfile = fragmentProfile.findViewById(R.id.name_profile)
        surnameProfile = fragmentProfile.findViewById(R.id.surname_profile)
        emailProfile = fragmentProfile.findViewById(R.id.email_profile)
        passwordProfile = fragmentProfile.findViewById(R.id.password_profile)
        return fragmentProfile
    }


}