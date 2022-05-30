package com.example.englishlearning.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.example.englishlearning.R
import com.example.englishlearning.UserActivity
import com.example.englishlearning.helpers.ErrorUtils
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.Users
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class RegistrationSecondStepFragment : Fragment() {

    private val args : RegistrationSecondStepFragmentArgs by navArgs()
    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var rePasswordText : EditText
    private lateinit var registrationButton : AppCompatButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationButton.setOnClickListener {
            var check = true
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText.text.toString()).matches() || emailText.text.isNullOrEmpty()){
                check = false
                emailText.setError("Верно заполните адрес электронной почты")
            }
            if (passwordText.text.toString().length < 5){
                check = false
                passwordText.setError("Пароль должен состоять ")
            }
            if (rePasswordText.text.toString() != passwordText.text.toString()){
                check = false
                rePasswordText.setError("Пароль не совпадают")
            }
            if (check){
                val newUser = Users(0, args.surname, args.name, args.levelId,emailText.text.toString(), passwordText.text.toString(), args.testId)
                registrationUser(newUser)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val secondStep = inflater.inflate(R.layout.fragment_registration_second_step, container, false)
        emailText = secondStep.findViewById(R.id.email_registration)
        passwordText = secondStep.findViewById(R.id.password_registration)
        rePasswordText = secondStep.findViewById(R.id.re_password_registration)
        registrationButton = secondStep.findViewById(R.id.registration_complete)
        return secondStep
    }

    private fun registrationUser(newUser : Users){
        val buildServices = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestServices = buildServices.addUser(newUser)
        requestServices.enqueue(object : Callback<Users>{
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful){
                    val userId = PaperUserId(response.body()!!.id)
                    Paper.book().write("userId",userId)
                    Toast.makeText(context, "Пользователь зарегистрирован", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, UserActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                }else{
                    //Toast.makeText(context, "${response.errorBody()}", Toast.LENGTH_SHORT).show()
                    var apiError = ErrorUtils().parseError(response)
                    Toast.makeText(context, "${apiError!!.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }


}