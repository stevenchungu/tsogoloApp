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
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.tsogolo.R
import com.example.tsogolo.ui.profile.ProfilingActivity
import com.example.tsogolo.ui.theme.TsogoloTheme
import com.example.tsogolo.ui.theme.Typography

class FirstLaunchActivity : ComponentActivity() {
    @OptIn(ExperimentalUnitApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TsogoloTheme(this.applicationContext) {

                Column(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.background)) {
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

                    Text(
                        text = "Identify your suitable career based on your personality and class performance.",
                        style = Typography.body1.copy(
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colors.onSurface,
                            letterSpacing = TextUnit(4f, TextUnitType.Sp)
                        ),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { openProfilingActivity() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp)
                            .height(48.dp),
                    ) {
                        Text(text = "Get Started")
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