package com.example.tsogolo.ui.career

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.ui.personality.PersonalityTestActivity
import com.example.tsogolo.ui.theme.TsogoloTheme

class CareerSearchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[CareerSearchViewModel::class.java]

//        viewModel.initialize(this.applicationContext, intent.getIntExtra(CareerFinderActivity.EXTRA_USER_ID, -1)) {
//            CareerGuideActivity.start(this, viewModel.user)
//            finish()
//        }


        setContent {
            TsogoloTheme(this.applicationContext) {
//                CareerSearchLayout(viewModel) {
//                    finish()
//                }
            }
        }
    }

}