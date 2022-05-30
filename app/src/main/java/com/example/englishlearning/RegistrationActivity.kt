package com.example.englishlearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController


class RegistrationActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var navController : NavController
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)




        //findNavController(R.id.host_fragment).setGraph(R.navigation.nav_graph, intent.extras)

        val hostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        navController = hostFragment.findNavController()
        appBarConfig = AppBarConfiguration(
            setOf(R.id.registrationFirstStepFragment, R.id.registrationSecondStepFragment)
        )

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}