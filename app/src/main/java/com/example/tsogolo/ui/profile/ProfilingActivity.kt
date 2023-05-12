package com.example.tsogolo.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.model.User
import com.example.tsogolo.ui.theme.TsogoloTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ProfilingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[ProfilingViewModel::class.java]

        var uid: Int? = intent.getIntExtra(USER_ID, -1)

        if (uid == -1) uid = null

        CoroutineScope(IO).launch {
            viewModel.initialize(this@ProfilingActivity.applicationContext, uid) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(RESULT, true)
                })
                finish()
            }
        }

        setContent {
            TsogoloTheme(this.applicationContext) {
                ProfilingLayout(viewModel) {
                    finish()
                }
            }
        }
    }

    companion object {
        const val RESULT = "result"
        private const val USER_ID = "com.tsogolo.Uid"

        fun editProfile(context: Context, user: User) {
            context.startActivity(Intent(context, ProfilingActivity::class.java)
                .apply {
                    putExtra(USER_ID, user.id)
                })
        }
    }
}