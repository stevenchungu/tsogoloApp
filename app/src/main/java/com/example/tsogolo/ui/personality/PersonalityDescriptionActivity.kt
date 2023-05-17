package com.example.tsogolo.ui.personality

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.Personality
import com.example.tsogolo.ui.theme.TsogoloTheme
import com.example.tsogolo.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class PersonalityDescriptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val personality = mutableStateOf(Personality())
        val personality2 = mutableStateOf(Personality())

        CoroutineScope(IO).launch {
            val ps = TsogoloDatabase.getInstance(this@PersonalityDescriptionActivity)
                .personalityDao().getAllOf(intent.getIntExtra(EXTRA_USER_ID, -1))

            personality.value = ps[0]
            personality2.value = ps[1]
        }

        setContent {
            TsogoloTheme(this.applicationContext) {

              Box(modifier = Modifier
                  .fillMaxSize()
                  .background(color = MaterialTheme.colors.background)
              ){
                  Column(modifier = Modifier
                      .verticalScroll(rememberScrollState())
                      .background(color = MaterialTheme.colors.background)
                      .fillMaxSize()) {
                      TopAppBar(title = { Text("Personality Overview") }, navigationIcon = {
                          Icon(
                              imageVector = Icons.Default.ArrowBack,
                              contentDescription = "Back Arrow",
                              Modifier.clickable { finish() },

                              )
                      },
                          backgroundColor = MaterialTheme.colors.background,

                          )

                      Column(modifier = Modifier
                          .padding(all = 16.dp)
                          .align(Alignment.CenterHorizontally)) {
                          Text(
                              text = "${personality.value.type}",
                              style = Typography.h4.copy(
                                  fontFamily = FontFamily.SansSerif
                              ),
                              textAlign = TextAlign.Center
                          )
                          Text(
                              text = "Your secondary personality is ${personality2.value.type}",
                              style = Typography.caption.copy(
                                  fontFamily = FontFamily.SansSerif
                              ),
                              textAlign = TextAlign.Center
                          )
                      }

                      Divider(Modifier.padding(bottom = 16.dp))

                      Text(
                          text = "${personality.value.description}",
                          style = Typography.body2.copy(
                              color = MaterialTheme.colors.onSurface,

                              ),
                          modifier = Modifier.padding(16.dp),
                          textAlign = TextAlign.Center
                      )

                      Spacer(modifier = Modifier.height(4.dp))
                      Button(onClick = { finish() },
                          modifier = Modifier
                              .fillMaxWidth()
                              .padding(all = 16.dp)
                              .height(48.dp),
                          colors = ButtonDefaults.buttonColors(
                              backgroundColor = Color(0xFF0eF7729)),

                          ) {
                          Text(text = "DONE",
                              color = Color.White
                          )
                      }
                      Spacer(modifier = Modifier.height(16.dp))
                  }
              }

            }
        }
    }

    companion object {
        private const val EXTRA_USER_ID = "personality_id"

        @JvmStatic
        fun start(context: Context, userId: Int) {
            val starter = Intent(context, PersonalityDescriptionActivity::class.java)
                .putExtra(EXTRA_USER_ID, userId)
            context.startActivity(starter)
        }
    }
}