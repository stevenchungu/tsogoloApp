package com.example.tsogolo.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.tsogolo.MainActivity
import com.example.tsogolo.R
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.ui.explore.ExploreActivity
import com.example.tsogolo.ui.jobs.JobsActivity
import com.example.tsogolo.ui.profile.ProfilingActivity
import com.example.tsogolo.ui.theme.TsogoloTheme
import com.example.tsogolo.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FirstLaunchActivity : ComponentActivity() {
    @OptIn(ExperimentalUnitApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = TsogoloDatabase.getInstance(this.applicationContext)
        setContent {
            TsogoloTheme(this.applicationContext) {

                Column(modifier = Modifier
                    //.padding(1.dp)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.background)) {
                    Box(modifier = Modifier
                        .padding(all = 32.dp)
                        .align(Alignment.CenterHorizontally)) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_school_24),
                            contentDescription = "",
                            modifier = Modifier.size(128.dp),
                            colorFilter = ColorFilter.tint(Color(0xFF0eF7729))
                        )
                    }
                    Box(modifier = Modifier
                        .padding(all = 32.dp)
                        .align(Alignment.CenterHorizontally)) {
                        Text(
                            text = "Tsogolo",
                            style = Typography.h4.copy(
                                fontFamily = FontFamily.Cursive
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(modifier = Modifier
                        .align(Alignment.CenterHorizontally)){
                        Text(text = "Discover Your Path")
                    }




                    Divider(
                        Modifier
                            .padding(vertical = 32.dp))

                    Spacer(modifier = Modifier.height(32.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Button(onClick = {

                                CoroutineScope(Dispatchers.IO).launch {
                                    db.userDao().getAll().collectLatest {
                                        if (it.isEmpty()) {
                                            openProfilingActivity()
                                        } else {
                                            MainActivity.start(this@FirstLaunchActivity)

                                        }
                                    }
                                }
                                },
                                modifier = Modifier
                                    .width(200.dp)
                                    .padding(all = 5.dp)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF0eF7729),
                                ),
                            ) {
                                Text(text = "Career Guide",
                                    color = Color.White,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(32.dp))

                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly

                        ) {
                            Button(
                                onClick = { openExploreActivity() },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(all = 5.dp)
                                    .height(48.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF0eF7729))
                            ) {
                                Text(text = "Explore Career",
                                    color = Color.White,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }

                            Button(
                                onClick = {
                                    this@FirstLaunchActivity.startActivity(Intent(this@FirstLaunchActivity, JobsActivity::class.java))
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(all = 5.dp)
                                    .height(48.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF0eF7729))
                            ) {
                                Text(text = "Job Search",
                                    color = Color.White,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }

                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }


    fun openProfilingActivity() {
        val intent = Intent(this, ProfilingActivity::class.java)
        resultLauncher.launch(intent)
    }

    fun openExploreActivity() {
        val intent = Intent(this, ExploreActivity::class.java)
        startActivity(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK
            && result.data?.getBooleanExtra(ProfilingActivity.RESULT, false) == true) {
            setResult(RESULT_OK, result.data)
            finish()
        } else {
            val intent = Intent(this, FirstLaunchActivity::class.java)
            startActivity(intent)
        }
    }

}
