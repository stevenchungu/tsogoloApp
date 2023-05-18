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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.tsogolo.R
import com.example.tsogolo.ui.career.CareerFinderActivity
import com.example.tsogolo.ui.explore.ExploreActivity
//import com.example.tsogolo.ui.explore.ExploreActivity
//import com.example.tsogolo.ui.explore.ExploreActivity
import com.example.tsogolo.ui.profile.ProfilingActivity
import com.example.tsogolo.ui.theme.TsogoloTheme
import com.example.tsogolo.ui.theme.Typography

class FirstLaunchActivity : ComponentActivity() {
    @OptIn(ExperimentalUnitApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TsogoloTheme(this.applicationContext) {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.job),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // rest of your content
                    Column(modifier = Modifier
                        .fillMaxSize()
//                        .background(color = MaterialTheme.colors.background)
                    ) {
                        Box(modifier = Modifier
                            .padding(all = 32.dp)
                            .align(Alignment.CenterHorizontally)) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_school_24),
                                contentDescription = "",
                                modifier = Modifier.size(128.dp),
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary)
                            )
                        }
                        Box(modifier = Modifier
                            .padding(all = 32.dp)
                            .align(Alignment.CenterHorizontally)) {
                            Text(
                                text = "Tsogolo",
                                style = Typography.h4.copy(
                                    color = MaterialTheme.colors.primary,
                                    fontFamily = FontFamily.Cursive
                                ),
                                textAlign = TextAlign.Center
                            )
                        }

                        Divider(
                            Modifier
                                .padding(vertical = 32.dp)
                                .background(MaterialTheme.colors.primary))

//                        Text(
//                            text = "Identify your suitable career based on your personality and class performance.",
//                            style = Typography.body1.copy(
//                                fontFamily = FontFamily.Serif,
//                                color = MaterialTheme.colors.onSurface,
//                                letterSpacing = TextUnit(4f, TextUnitType.Sp)
//                            ),
//                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
//                            textAlign = TextAlign.Center
//                        )


                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))


                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),

                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = { openProfilingActivity() },
                                    modifier = Modifier
                                        .height(100.dp),
//                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
//                                        backgroundColor = Color.Blue
                                        backgroundColor = Color.Blue.copy(alpha = 0.0f) // Set higher alpha value here
                                    ),
//                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.huma),
                                            contentDescription = "Button icon",
                                            tint = Color.Black,
                                            modifier = Modifier.size(60.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Career Guidance",
                                            style = MaterialTheme.typography.button,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(top = 16.dp).padding(bottom = 16.dp)
                            ) {
                                Button(
                                    onClick = { openExploreActivity() },
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(150.dp),
                                    colors = ButtonDefaults.buttonColors(
//                                        backgroundColor = Color.Blue
                                        backgroundColor = Color.Blue.copy(alpha = 0.0f) // Set higher alpha value here
                                    ),
//                                shape = CircleShape
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.huma),
                                            contentDescription = "Button icon",
                                            tint = Color.Black,
                                            modifier = Modifier.size(60.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "explore",
                                            style = MaterialTheme.typography.button,
                                            color = Color.Black
                                        )
                                    }
                                }



                                Button(
                                    onClick = { /* Handle second button click */ },
                                    modifier = Modifier
                                        .padding(all = 0.dp)
                                        .height(100.dp)
                                        .width(150.dp),
                                    colors = ButtonDefaults.buttonColors(
//                                        backgroundColor = Color.Blue
                                        backgroundColor = Color.Blue.copy(alpha = 0.0f) // Set higher alpha value here
                                    ),
//                                shape = CircleShape
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.huma),
                                            contentDescription = "Button icon",
                                            tint = Color.Black,
                                            modifier = Modifier.size(60.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "JOb Search",
                                            style = MaterialTheme.typography.button,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }



            }
        }
    }


    fun openProfilingActivity() {
        val intent = Intent(this, ProfilingActivity::class.java)
        resultLauncher.launch(intent)
//        startActivity(intent)
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
            setResult(RESULT_CANCELED)
            finish()
        }
    }

}