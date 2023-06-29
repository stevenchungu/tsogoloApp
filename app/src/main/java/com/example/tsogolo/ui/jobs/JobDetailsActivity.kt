package com.example.tsogolo.ui.jobs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.R
import com.example.tsogolo.models.Job
import com.example.tsogolo.ui.theme.TsogoloTheme
import com.example.tsogolo.ui.theme.Typography
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.core.content.ContextCompat.startActivity

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),

            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

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
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Job Description",
                        style = Typography.body2.copy(
                            color = MaterialTheme.colors.onSurface,

                            ),
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = job.summary,
                        style = MaterialTheme.typography.body1,
                        fontSize = 16.sp
                    )

                    // Inside the Compose function
                    OpenLinkInBrowser(job.link)



                }
            }


        }
    }
}

@Composable
fun OpenLinkInBrowser(link: String) {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black)) {
            append("Link: ")
        }
        append("\n")
        pushStyle(
            SpanStyle(
            color = Color.Blue,
            textDecoration = TextDecoration.Underline
        )
        )
        append(link)
    }

    ClickableText(
        text = annotatedString,
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            context.startActivity(intent)
        },
        style = MaterialTheme.typography.body1.merge(
            TextStyle(fontSize = 16.sp)
        )
    )
}
