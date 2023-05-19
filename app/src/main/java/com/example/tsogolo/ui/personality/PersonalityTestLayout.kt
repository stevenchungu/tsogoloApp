package com.example.tsogolo.ui.personality

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.ui.theme.Typography

@Composable
fun PersonalityTestLayout(
    personalityTestViewModel: PersonalityTestViewModel = viewModel(),
    backArrowClicked: () -> Unit
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.background)
    ){
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()) {
            TopAppBar(
                title = { Text("Personality Test") },
                navigationIcon = { Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Arrow",
                Modifier.clickable { backArrowClicked() },

                )},
                backgroundColor = MaterialTheme.colors.background,

                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ){
                        Box(
                            contentAlignment = Alignment.Center

                        ) {

                            CircularProgressIndicator(
                                progress = personalityTestViewModel.progressValue.value,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = "${(personalityTestViewModel.progressValue.value * 100).toInt()}%",
                                style = TextStyle(fontSize = 10.sp),
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }

            )

            Box(modifier = Modifier.padding(all = 16.dp)) {
                Text(
                    text = "Submit button will be enabled when all the questions have been answered.\n\nAnswer Honestly!",
                    style = Typography.caption.copy(
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }

            Divider(Modifier.padding(bottom = 12.dp))

            Text(
                text = personalityTestViewModel.question.value.question,
                style = Typography.body1.copy(color = MaterialTheme.colors.onSurface, fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = personalityTestViewModel.question.value.agreed.value,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF0eF7729)
                        ),
                        onClick = { personalityTestViewModel.questionResponded(true) }
                    )
                    Text(text = "Agree",
                        modifier = Modifier.padding(start = 4.dp),
                        textAlign = TextAlign.Center,
                        style = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                    )
                }

                Spacer(modifier = Modifier.width(64.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = personalityTestViewModel.question.value.denied.value,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF0eF7729)
                        ),
                        onClick = { personalityTestViewModel.questionResponded(false) }
                    )

                    Text(text = "Disagree",
                        modifier = Modifier.padding(start = 4.dp),
                        textAlign = TextAlign.Center,
                        style = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                    )
                }
            }

            Divider(Modifier.padding(vertical = 16.dp))

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable{  personalityTestViewModel.previousQuestion()},
                            tint = MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.width(80.dp))

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable { personalityTestViewModel.nextQuestion() },
                    tint = MaterialTheme.colors.onSurface
                )
            }

            Button(onClick = { personalityTestViewModel.questionsSubmitted() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 32.dp)
                    .height(56.dp),
                enabled = personalityTestViewModel.canSubmit.value,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0eF7729)
                )
            ) {
                Text(text = "Submit",
                    color = Color.White
                )
            }
        }
    }

}