package com.example.tsogolo.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.tsogolo.ui.components.NumberPicker
import com.example.tsogolo.ui.theme.Typography

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ProfilingLayout(viewModel: ProfilingViewModel, backArrowClicked: () -> Unit) {
    val stateData = viewModel.profileData

    Column(modifier = Modifier.background(color = MaterialTheme.colors.background).fillMaxSize()) {
        TopAppBar(
            title = { Text("Create Profile") },
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary,
            navigationIcon = { Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Arrow",
                Modifier.clickable { backArrowClicked() },
                tint = MaterialTheme.colors.primary
            ) },
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(
                text = "Fill the form below then click the save profile button.",
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp).align(Alignment.CenterHorizontally),
                style = Typography.body1.copy(color = MaterialTheme.colors.primary, fontFamily = FontFamily.Cursive)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = stateData.value.name.value, onValueChange = {viewModel.nameChanged(it)},
                    label = { Text(text = "Enter your name") },
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .padding(all = 8.dp),
                    textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                )

                Divider(modifier = Modifier.padding(top = 16.dp))

                Text(text = "Date of Birth: (Year/Month/Day)",
                    modifier = Modifier
                        .padding(bottom = 8.dp, top = 16.dp),
                    style = Typography.body2.copy(color = MaterialTheme.colors.onSurface)
                )
                Row(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) {
                    NumberPicker(
                        state = stateData.value.year,
                        range = viewModel.years,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.CenterVertically),
                        text = "/",
                        style = TextStyle(
                            fontSize = TextUnit(28f, TextUnitType.Sp),
                            color = Color.Gray,
                            fontWeight = FontWeight.Light,
                        )
                    )
                    NumberPicker(
                        state = stateData.value.month,
                        range = viewModel.months,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.CenterVertically),
                        text = "/",
                        style = TextStyle(
                            fontSize = TextUnit(28f, TextUnitType.Sp),
                            color = Color.Gray,
                            fontWeight = FontWeight.Light
                        )
                    )
                    NumberPicker(
                        state = stateData.value.day,
                        range = viewModel.days,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                    )
                }

                Divider()

                Column {
                    Text(text = "Education Level",
                        modifier = Modifier
                            .padding(bottom = 8.dp, top = 16.dp),
                        style = Typography.body2.copy(color = MaterialTheme.colors.onSurface)
                    )
                    viewModel.eduLevels.forEach { level ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = level == stateData.value.eduLevel.value,
                                onClick = { viewModel.onEdulevelSelected(level) })
                            Text(text = level,
                                modifier = Modifier.padding(start = 16.dp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }

                Divider()

                Column {
                    Text(text = "Grades (Points Scored in Chosen Education Level) A is 1, B is 2...",
                        modifier = Modifier
                            .padding(bottom = 8.dp, top = 16.dp),
                        style = Typography.body2.copy(color = MaterialTheme.colors.primary)
                    )
                    stateData.value.subjectGrades.value.forEachIndexed { i, it ->
                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(
                                text = it.first.name ?: "Unknown",
                                modifier = Modifier
                                    .width(148.dp)
                                    .align(Alignment.CenterVertically),
                                color = MaterialTheme.colors.onSurface
                            )
                            OutlinedTextField(value = it.second,
                                onValueChange = {viewModel.onGradeChange(i, it)},
                                placeholder = { Text(text = "Points") },
                                isError = !it.second.isDigitsOnly() && it.second.isNotEmpty(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(onClick = { viewModel.saveProfile() },
                    modifier = Modifier.fillMaxWidth().padding(all = 16.dp).height(56.dp),
                    enabled = stateData.value.name.value.isNotBlank()
                            && !stateData.value.saving.value && !stateData.value.saved.value
                ) {
                    Text(text = when {
                        stateData.value.saving.value -> "Saving..."
                        stateData.value.saved.value -> "Saved"
                        else -> "Save Profile"
                    }
                    )
                }
            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TsogoloTheme {
        ProfilingLayout()
    }
}*/
