package com.example.englishlearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.englishlearning.databinding.ActivityFirstLaunchBinding

class FirstLaunchActivity : AppCompatActivity() {

    lateinit var infoBlock : ConstraintLayout
    lateinit var binding : ActivityFirstLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_launch)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first_launch)



        binding.startTestButton.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

        binding.authorizationButton.setOnClickListener {
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            //finish()
        }

    }
}