package com.example.tsogolo.ui.personality

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.model.User
import com.example.tsogolo.ui.theme.TsogoloTheme

class PersonalityTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[PersonalityTestViewModel::class.java]

        val uid = intent.getIntExtra(EXTRA_USER_ID, -1)

        viewModel.initialize(this.applicationContext, uid) {
            PersonalityDescriptionActivity.start(this, uid)
            finish()
        }

        setContent {
            TsogoloTheme(this.applicationContext) {
                PersonalityTestLayout(viewModel) {
                    finish()
                }
            }
        }
    }

    companion object {
        private const val EXTRA_USER_ID = "user_id"

        @JvmStatic
        fun start(context: Context, user: User) {
            val starter = Intent(context, PersonalityTestActivity::class.java)
                .putExtra(EXTRA_USER_ID, user.id!!)
            context.startActivity(starter)
        }

        fun starter(context: Context, user: User) = Intent(context, PersonalityTestActivity::class.java)
            .putExtra(EXTRA_USER_ID, user.id!!)
    }
}