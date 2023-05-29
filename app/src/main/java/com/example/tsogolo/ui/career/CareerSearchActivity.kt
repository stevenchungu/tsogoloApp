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

        viewModel.hasNoPersonality = {
            AlertDialog.Builder(this)
                .setMessage("It seems you haven't taken a personality test yet." +
                        "\n\nWould you like to identify your personality?")
                .setNegativeButton("Later", null)
                .setPositiveButton("Yes") {_,_ ->
                    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        viewModel.updatePersonalities()
                    }.launch(PersonalityTestActivity.starter(this, viewModel.user))
                }.create().show()
        }

        setContent {
            TsogoloTheme(this.applicationContext) {
                CareerSearchLayout(viewModel) {
                    finish()
                }
            }
        }
    }

}