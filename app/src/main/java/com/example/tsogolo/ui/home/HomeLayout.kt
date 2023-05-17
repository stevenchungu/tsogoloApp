package com.example.tsogolo.ui.home

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.model.User
import com.example.tsogolo.ui.career.CareerGuideActivity
import com.example.tsogolo.ui.components.CareerItem
import com.example.tsogolo.ui.components.CollegeItem
import com.example.tsogolo.ui.components.ProgramItem
import com.example.tsogolo.ui.components.SubjectItem
import com.example.tsogolo.ui.personality.PersonalityDescriptionActivity
import com.example.tsogolo.ui.theme.Typography
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeLayout(
    homeViewModel: HomeViewModel = viewModel(),
    context: Context,
    userLongClicked: (User) -> Unit = {},
    launchPersonalityTest: () -> Unit = {},
    launchCareerFinder: () -> Unit = {},
    editProfile: (User) -> Unit = {}
) {

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .background(color = MaterialTheme.colors.background)
        .padding(8.dp)
        .fillMaxSize()) {

        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .background(color = MaterialTheme.colors.background)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround) {
            items(homeViewModel.users.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                        .combinedClickable(onLongClick = {
                            userLongClicked(it)
                        }) { homeViewModel.userSwitched(it) }) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = if (homeViewModel.activeUser.value.id == it.id) Color(0xFF0eF7729)
                                else Color.Gray,
                                shape = CircleShape
                            )
                            .border(2.dp, color = Color.DarkGray, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = (it.name ?: "U").first().toString().uppercase(Locale.ENGLISH),

                            style = Typography.h6.copy(
                                color = Color.White,
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                    }
                    Text(
                        text = (it.name?.replaceFirstChar { it.uppercase() } ?: "Unknown"),
                        style = Typography.caption.copy(
                            color = if (homeViewModel.activeUser.value.id == it.id) Color.Black else Color.Gray,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(elevation = 2.dp, modifier = Modifier
            .shadow(2.dp)
            .fillMaxWidth(),
            color = MaterialTheme.colors.background
        ) {
            val user = homeViewModel.activeUser.value

            Column(modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()) {

                Row {
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Profile",
                        style = Typography.caption.copy(
                            color = Color(0xFF0eF7729),
                            fontFamily = FontFamily.SansSerif
                        )
                    )

                    Spacer(modifier = Modifier.fillMaxWidth(0.7f))

                    IconButton(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        onClick = { editProfile(homeViewModel.activeUser.value) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "",
                            tint = Color(0xFF0eF7729)
                        )
                    }
                }

                ProfileEntry(key = "Name", value = user.name?.replaceFirstChar { it.uppercase() } ?: "Unknown")

                Spacer(modifier = Modifier.height(4.dp))

                ProfileEntry(key = "Education Level", value = user.eduLevel)

                Spacer(modifier = Modifier.height(4.dp))


                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Personality",
                        style = Typography.subtitle2.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        ),
                        modifier = Modifier.width(128.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = homeViewModel.activePersonalities.value.foldIndexed("") {i, acc, personality ->
                            "$acc${personality.type} "
                        },
                        style = Typography.subtitle2.copy(
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable {
                            PersonalityDescriptionActivity.start(context, homeViewModel.activeUser.value.id!!)
                        }
                    )
                }

                Divider(modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(Color.Black))

                Button(
                    modifier = Modifier
                        .padding(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF0eF7729)),
                    onClick = { launchPersonalityTest() }
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Take Personality Test",
                        style = Typography.button.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White
                        ),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(elevation = 2.dp, modifier = Modifier
            .shadow(2.dp)
            .fillMaxWidth(),
            color = MaterialTheme.colors.background
        ) {
            val careers = homeViewModel.careers.value
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            CareerGuideActivity.start(
                                context,
                                homeViewModel.activeUser.value
                            )
                        },
                    text = "Preferred Careers",
                    style = Typography.caption.copy(
                        color = Color(0xFF0eF7729),
                        fontFamily = FontFamily.SansSerif
                    ),
                    textDecoration = TextDecoration.Underline
                )
                careers.forEachIndexed {i, it ->
                    CareerItem(career = it)
                    if (i != careers.lastIndex) {
                        Divider(modifier = Modifier.padding(vertical = 6.dp))
                    }
                }

                Divider(modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(Color.Black))

                Button(onClick = { launchCareerFinder() },
                    modifier = Modifier
                        .padding(bottom = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF0eF7729))
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Identify Suitable Career",
                        style = Typography.button.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White
                        ),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier
            .shadow(2.dp)
            .background(color = MaterialTheme.colors.background)) {
            if (homeViewModel.activeUser.value.eduLevel != User.MSCE) {
                val subjects = homeViewModel.subjects.value

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Subjects To Focus On",
                        style = Typography.caption.copy(
                            color = Color.Black,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    subjects.forEachIndexed {i, it ->
                        SubjectItem(subject = it)
                        if (i != subjects.lastIndex) {
                            Divider(modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }
            }

            val programs = homeViewModel.programs.value
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Programs To Study",
                    style = Typography.caption.copy(
                        fontFamily = FontFamily.SansSerif
                    )
                )
                programs.forEachIndexed {i, it ->
                    var isExpanded by remember { mutableStateOf(false) }
                    Divider()
                    ProgramItem(program = it)
                    Text(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clickable { isExpanded = !isExpanded },
                        text = "Colleges",
                        style = Typography.caption.copy(
                            color = Color(0xFF0eF7729),
                            fontFamily = FontFamily.SansSerif
                        ),
                        textDecoration = TextDecoration.Underline
                    )
                    if (isExpanded) {
                        Column(
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            homeViewModel.colleges.value[it]?.forEach {
                                CollegeItem(college = it)
                            }
                        }
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
private fun ProfileEntry(key: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = key,
            style = Typography.subtitle2.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            ),
            modifier = Modifier.width(128.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = value,
            style = Typography.subtitle2.copy(
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        )
    }
}