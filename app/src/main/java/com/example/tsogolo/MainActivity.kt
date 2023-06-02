package com.example.tsogolo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.descendants
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.databinding.ActivityMainBinding
import com.example.tsogolo.model.User
import com.example.tsogolo.ui.FirstLaunchActivity
import com.example.tsogolo.ui.profile.ProfilingActivity
import com.example.tsogolo.util.AppPreferences
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var users = listOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = TsogoloDatabase.getInstance(this.applicationContext)
        CoroutineScope(IO).launch {
            db.userDao().getAll().collectLatest {
                if (it.isEmpty()) {
                    openFirstLaunchActivity()
                } else {
                    users = it

                }
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        AppPreferences.getInstance(this@MainActivity.applicationContext).isDarkTheme
            .asLiveData().observe(this) {
                if (it) {
                    binding.appBarMain.toolbar.popupTheme = R.style.ThemeOverlay_AppCompat_Dark
                    binding.appBarMain.toolbar.setBackgroundColor(resources.getColor(R.color.dark_blue))
                } else {
                    binding.appBarMain.toolbar.popupTheme = R.style.ThemeOverlay_AppCompat_Light
                    binding.appBarMain.toolbar.setBackgroundColor(resources.getColor(R.color.white))
                }
            }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_blog, R.id.nav_about), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.nav_exit -> {
                    AlertDialog.Builder(this)
                        .setMessage(R.string.exit_warning)
                        .setPositiveButton("YES") {_,_ ->
                            finishAndRemoveTask()
                        }.setNegativeButton("NO", null)
                        .create().show()
                    true
                } else -> {
//                    navController.navigate(it.itemId)
                    false
                }
            }
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                //TODO("Not yet implemented")
            }

            override fun onDrawerOpened(drawerView: View) {
                val pref = AppPreferences.getInstance(this@MainActivity.applicationContext)
                val themeToggler = findViewById<TextView>(R.id.theme_toggler)
                val header = findViewById<ConstraintLayout>(R.id.header)
                val setToggleImage = {
                    themeToggler.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        if (pref.isDarkTheme.value)
                            R.drawable.ic_baseline_toggle_on_24
                        else R.drawable.ic_baseline_toggle_off_24,
                        0
                    )

                    if (pref.isDarkTheme.value) {
                        themeToggler.text = getString(R.string.dark_mode)
                        binding.navView.setBackgroundColor(resources.getColor(R.color.dark_blue))
                        binding.navView.itemTextColor = ColorStateList.valueOf(resources.getColor(R.color.white))
                        binding.navView.itemIconTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
                        header.descendants.forEach {
                            if (it is TextView) it.setTextColor(resources.getColor(R.color.white))
                        }
                    } else {
                        themeToggler.text = getString(R.string.light_mode)
                        binding.navView.setBackgroundColor(resources.getColor(R.color.white))
                        binding.navView.itemTextColor = ColorStateList.valueOf(resources.getColor(R.color.dark_blue))
                        binding.navView.itemIconTintList = ColorStateList.valueOf(resources.getColor(R.color.dark_blue))
                        header.descendants.forEach {
                            if (it is TextView) it.setTextColor(resources.getColor(R.color.dark_blue))
                        }
                    }
                }
                setToggleImage()
                themeToggler.setOnClickListener {
                    pref.setIsDarkTheme(!pref.isDarkTheme.value)
                    setToggleImage()
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                //TODO("Not yet implemented")
            }

            override fun onDrawerStateChanged(newState: Int) {
                //TODO("Not yet implemented")
            }

        })
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun openFirstLaunchActivity() {
        val intent = Intent(this, FirstLaunchActivity::class.java)
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK
            && result.data?.getBooleanExtra(ProfilingActivity.RESULT, false) == true) {
            CoroutineScope(IO).launch {
                TsogoloDatabase.getInstance(this@MainActivity.applicationContext)
                    .userDao().getAll().collectLatest {
                    users = it
                }
            }
        } else {
            finishAndRemoveTask()
        }
    }
   companion object{
       @JvmStatic
       fun start(context: Context){
           val starter = Intent(context, MainActivity::class.java)
           context.startActivity(starter)
       }
   }
}