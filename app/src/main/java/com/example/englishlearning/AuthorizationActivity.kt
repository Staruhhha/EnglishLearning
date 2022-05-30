package com.example.englishlearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.englishlearning.databinding.ActivityAuthorizationBinding
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.Users
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        Paper.init(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_authorization)
        binding.authorizationComplete.setOnClickListener {
            val user = Users(0, "", "", "", binding.emailAuthorization.text.toString(), binding.passwordAuthorization.text.toString(), 0)
            authorUsers(user)
            binding.layoutAuth.visibility = View.INVISIBLE
            binding.authProgBar.visibility = View.VISIBLE
        }

    }

    private fun authorUsers(user : Users){
        val serviceBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = serviceBuilder.authUser(user)
        requestCall.enqueue(object : Callback<Users>{
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful){
                    val userId = PaperUserId(response.body()!!.id)
                    Paper.book().write("userId",userId)
                    val intent = Intent(this@AuthorizationActivity, UserActivity::class.java)
                    binding.authProgBar.visibility = View.INVISIBLE
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                }else{
                    Toast.makeText(this@AuthorizationActivity, "Пользователя с такими данными нет в системе", Toast.LENGTH_SHORT).show()
                    binding.layoutAuth.visibility = View.VISIBLE
                    binding.authProgBar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(this@AuthorizationActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                binding.layoutAuth.visibility = View.VISIBLE
                binding.authProgBar.visibility = View.INVISIBLE
            }

        })
    }
}