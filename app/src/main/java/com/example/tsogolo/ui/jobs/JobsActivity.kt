package com.example.tsogolo.ui.jobs

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tsogolo.R
import com.example.tsogolo.models.Job
import com.example.tsogolo.ui.theme.TsogoloTheme
import com.example.tsogolo.ui.theme.Typography

class JobsActivity : ComponentActivity() {
    private val viewModel: JobsViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.loadJobs(this )
        setContent {
            TsogoloTheme(this@JobsActivity) {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier
                        .background(color = androidx.compose.material.MaterialTheme.colors.background)
                        .fillMaxSize()
                ) {
                    TopAppBar(
                        title = { androidx.compose.material.Text("Jobs") },
                        backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
                        contentColor = Color.Black,
                        navigationIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back Arrow",
                                Modifier.clickable { finish() },
                                tint = Color.Black
                            )
                        }
                    )

                    Column(modifier = Modifier.fillMaxSize()) {
                        // Search Bar
                        SearchBar(viewModel = viewModel)

                        // Filter Chips
                       /* LazyRow(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)) {
                            items(viewModel.jobCategories.size) { category ->
                                FilterChip(
                                    selected = viewModel.selectedCategories.value.contains(viewModel.jobCategories[category]),
                                    colors = ChipDefaults.filterChipColors(
                                        selectedBackgroundColor = Color(0xFFeF7729)
                                    ),
                                    onClick = {
                                        viewModel.toggleCategorySelection(viewModel.jobCategories[category])
                                    }
                                ) {
                                  Text(text = viewModel.jobCategories[category])
                                }
                                Spacer(modifier = Modifier.size(8.dp))
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        */

                        // List of Jobs
                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
//                            val isLoading by viewModel.isLoading.collectAsState()


                            if (viewModel.isLoading.value) {
                                // Display a loading indicator
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            } else {
                                if (viewModel.jobs.value.isEmpty()) {
                                    // Display a network error message and a retry link
                                    item {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(text = "Network error. Please check your connection.")
                                            Spacer(modifier = Modifier.height(16.dp))
                                            ClickableText(
                                                text = buildAnnotatedString {
                                                    withStyle(
                                                        style = SpanStyle(
                                                            color = Color.Blue,
                                                            textDecoration = TextDecoration.Underline
                                                        )
                                                    ) {
                                                        append("Retry")
                                                    }
                                                },
                                                onClick = {
                                                    viewModel.loadJobs(this@JobsActivity)
                                                }
                                            )

                                        }
                                    }
                                } else {
                                    // Display the list of jobs
                                    items(viewModel.jobs.value.size) { job ->
                                        JobItem(
                                            job = viewModel.jobs.value[job],
                                            activity = this@JobsActivity
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun JobItem(job: Job, activity: JobsActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(activity, JobDetailsActivity::class.java)
                intent.putExtra("jobId", job)
                activity.startActivity(intent)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        /*// Company Image
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Company Logo",
            modifier = Modifier.size(64.dp)
        )*/

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = job.title,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = job.sector,
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,

            )
            Text(
                text = job.location,
                style = MaterialTheme.typography.body2,
                fontSize = 12.sp
            )
            Text(
                text = job.time,
                style = MaterialTheme.typography.body2,
                fontSize = 12.sp
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            /*CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.body2,
                        fontSize = 12.sp
                    )

                }
            }*/

        }
    }
}

@Composable
fun SearchBar(viewModel: JobsViewModel) {
    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        focusedIndicatorColor = Color.Gray,
        unfocusedIndicatorColor = Color.Gray,
        cursorColor = Color.Gray,
    )
    OutlinedTextField(
        value = viewModel.searchQuery.value,
        onValueChange = { viewModel.setSearchQuery(it)  },
        placeholder = { Text(text = "Search Jobs") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxWidth(),
        textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
    )

//    TextField(
//        value = viewModel.searchQuery.value,
//        onValueChange = { viewModel.setSearchQuery(it) },
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth(),
//        label = { Text("Search Jobs") },
//        leadingIcon = { Icon(Icons.Default.Search, "Search Icon") },
//        colors = textFieldColors,
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Text,
//            imeAction = ImeAction.Done
//        ),
//        keyboardActions = KeyboardActions(
//            onDone = { viewModel.updateJobs() }
//        )
//    )
}