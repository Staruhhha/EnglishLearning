package com.example.englishlearning.fragments

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.englishlearning.R
import com.example.englishlearning.UserActivity
import com.example.englishlearning.helpers.ErrorUtils
import com.example.englishlearning.helpers.PaperUserData
import com.example.englishlearning.models.Users
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationFirstStepFragment : Fragment() {

    lateinit var nameEditText : EditText
    lateinit var surnameEditText : EditText
    lateinit var goNextButton : AppCompatButton
    lateinit var googleButton : AppCompatButton
    private var RC_SIGN_IN = 0
    lateinit var mGoogleSignInClient : GoogleSignInClient
    private var level = ""
    private var idTest = 0


    var resultLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            var task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try{
            var account : GoogleSignInAccount = task!!.getResult(ApiException::class.java)
            var newUser = Users(0, if (account.familyName !=null) account.familyName!! else  "", if (account.givenName !=null) account.givenName!! else  account.displayName, level,account.email, "", idTest)
            registrationFromGoogle(newUser)
        }catch (e : ApiException){
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            mGoogleSignInClient.signOut()
        }
    }

    private fun registrationFromGoogle(user: Users){
        val buildServices = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestServices = buildServices.addUser(user)
        requestServices.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful){
                    Toast.makeText(context, "Пользователь зарегистрирован", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, UserActivity::class.java)
                    startActivity(intent)
                }else{
                    mGoogleSignInClient.signOut()
                    //Toast.makeText(context, "${response.errorBody()}", Toast.LENGTH_SHORT).show()
                    var apiError = ErrorUtils().parseError(response)
                    Toast.makeText(context, "${apiError!!.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                mGoogleSignInClient.signOut()
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        googleButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            resultLaunch.launch(signInIntent)

        }


        var userData : PaperUserData = Paper.book().read("userData")
        level = userData.level
        idTest = userData.testId


        goNextButton.setOnClickListener {
            var check = true
            if (nameEditText.text.isNullOrEmpty()){
                nameEditText.setError("Заполните имя")
                check = false
            }
            if (surnameEditText.text.isNullOrEmpty()){
                surnameEditText.setError("Заполните фамилию")
                check = false
            }
            if (check){
                val name = nameEditText.text.toString()
                val surname = surnameEditText.text.toString()
                val action = RegistrationFirstStepFragmentDirections.actionRegistrationFirstStepFragmentToRegistrationSecondStepFragment(name, surname, level, idTest)
                findNavController().navigate(action)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var firstStep = inflater.inflate(R.layout.fragment_registration_first_step, container, false)
        nameEditText = firstStep.findViewById(R.id.name_registration)
        surnameEditText = firstStep.findViewById(R.id.surname_registration)
        goNextButton = firstStep.findViewById(R.id.next_step_mail)
        googleButton = firstStep.findViewById(R.id.google_registration)

        var googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context!!, googleSignInOption)
        Paper.init(context)
        return firstStep
    }


}