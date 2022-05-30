package com.example.englishlearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.example.englishlearning.helpers.PaperUserId
import com.example.englishlearning.models.Users
import com.example.englishlearning.services.ApiRequest
import com.example.englishlearning.services.ServiceBuilder
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var label : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Paper.init(this)

        label = findViewById(R.id.label)

        label.visibility = View.INVISIBLE
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        label.visibility = View.VISIBLE
        label.startAnimation(animation)

        Handler().postDelayed({
            //val animation2 = AnimationUtils.loadAnimation(this, R.anim.lefttoright)
            //label.startAnimation(animation2)
            //Thread.sleep(1000)

            var readUsr : PaperUserId? = Paper.book().read("userId")
            if (readUsr == null){
                val intent = Intent(this, FirstLaunchActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }else{
                //Toast.makeText(this, "юзер есть ${readUsr.userId}", Toast.LENGTH_SHORT).show()
                loginUserById(readUsr.userId)
            }


        }, 3000)

    }

    private fun loginUserById(id : Int){
        val servBuilder = ServiceBuilder.buildServices(ApiRequest::class.java)
        val requestCall = servBuilder.getUserById(id)
        requestCall.enqueue(object : Callback<Users>{
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful){
                    if (response.body() != null){
                        val intent = Intent(this@MainActivity, UserActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }else{
                        Paper.book().destroy()
                        val intent = Intent(this@MainActivity, FirstLaunchActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                Paper.book().destroy()
            }

        })
    }

}