package com.example.tsogolo.ui.explore

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.ui.theme.TsogoloTheme

class ExploreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[ExploreViewModel::class.java]




        setContent {
            TsogoloTheme(this.applicationContext) {
                exploreCareer(viewModel) {
                    finish()
                }
            }
        }
    }
}

public data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val target: Class<out Activity>
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuCard(item: MenuItem, onclick: () -> Unit) {

    Card(
        onClick = onclick,
        modifier = Modifier
            .defaultMinSize(256.dp)
            .padding(2.dp),
        shape = RoundedCornerShape(8.dp),

        ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .padding(vertical = 32.dp)
            .fillMaxSize()) {
            Icon(
                imageVector = item.icon,
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 24.dp),
                tint = Color(0xFF0eF7729)
            )
            Text(
                text = item.title,
                modifier = Modifier,
                style = MaterialTheme.typography.button.copy(fontSize = 15.sp),
                textAlign = TextAlign.Center
            )
        }
    }
}

