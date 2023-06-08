package com.example.tsogolo.ui.jobs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.R
import com.example.tsogolo.models.Job
import com.example.tsogolo.ui.theme.TsogoloTheme

class JobDetailsActivity : ComponentActivity() {

    private val jobsViewModel: JobsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val job = intent.getSerializableExtra("jobId") as Job

        setContent {
            TsogoloTheme(this@JobDetailsActivity) {
                JobDetailsScreen(job, this@JobDetailsActivity)
            }
        }
    }
}

@Composable
fun JobDetailsScreen(job: Job, activity: ComponentActivity? = null) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Details") },
                backgroundColor = Color.White,
                contentColor = Color.Black,
                navigationIcon = {
                    IconButton(
                        onClick = { activity?.finish() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Arrow"
                        )
                    }
                }
            )
        },
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.job), // Replace with the actual image resource
                contentDescription = "Company Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = job.title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = job.sector,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = job.location,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = job.summary,
                style = MaterialTheme.typography.body1,
                fontSize = 16.sp
            )
        }
    }
}

