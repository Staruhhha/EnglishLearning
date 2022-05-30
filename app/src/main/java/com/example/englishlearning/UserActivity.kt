package com.example.englishlearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import io.paperdb.Paper

class UserActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var navController : NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var navView : NavigationView
    private lateinit var drawerLayout : DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        Paper.init(this)
        Paper.book().delete("userData")

        navView = findViewById(R.id.side_menu)
        drawerLayout = findViewById(R.id.user_drawer_layout)

        val hostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment_user) as NavHostFragment
        navController = hostFragment.findNavController()

        appBarConfig = AppBarConfiguration(
            setOf(R.id.auditionFragment, R.id.profileFragment, R.id.readingFragment),
            drawerLayout)

        toolbar = findViewById(R.id.toolbar_user)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfig)

        navView.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

}