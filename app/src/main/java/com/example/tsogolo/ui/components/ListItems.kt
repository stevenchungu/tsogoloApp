package com.example.tsogolo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.tsogolo.model.*
import com.example.tsogolo.ui.career.CareerData
import com.example.tsogolo.ui.theme.Typography
import com.example.tsogolo.util.Functions.getMoneyStringShort
import kotlin.math.roundToInt


@Composable
fun CareerItem(career: Career) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(
            text = (career.title ?: "Unknown") + " (MK${getMoneyStringShort(career.aas)}/Year)",
            style = Typography.subtitle2.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface
            )
        )
        Text(
            text = career.description ?: "Unknown",
            style = Typography.body2.copy(
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface
            )
        )
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun SearchItem(career: CareerData, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = career.title,
            style = Typography.subtitle2.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface,
                lineHeight = TextUnit(18f, TextUnitType.Sp)
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = career.description ?: "Unknown",
            style = Typography.body2.copy(
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun CareerCategoryList(careerCategories: Category) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(text = "Category ID: ${careerCategories.id}, Category Name: ${careerCategories.categoryName}")
    }

}

@Composable
fun ProgramItem(program: Program) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(
            text = (program.name ?: "Unknown"),
            style = Typography.subtitle2.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface
            )
        )
        Text(
            text = program.duration.toString() + " Years",
            style = Typography.body2.copy(
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface
            )
        )
    }
}

@Composable
fun SubjectItem(subject: Subject) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(
            text = (subject.name ?: "Unknown"),
            style = Typography.subtitle2.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface
            )
        )
        Text(
            text = subject.category,
            style = Typography.body2.copy(
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface
            )
        )
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun SelectableCareerItem(career: CareerData, onCheckedChange: () -> Unit) {

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onCheckedChange() }
    ) {
        Text(text = buildAnnotatedString {
            appendInlineContent("check")
            append(career.title + " (${getMoneyStringShort(career.aas)}/Year)")
        }, inlineContent = mapOf("check" to InlineTextContent(
            Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Checkbox(
                checked = career.isSelected,
                onCheckedChange = {onCheckedChange()},
                modifier = Modifier.padding(end = 8.dp)
            )
        }),
            style = Typography.subtitle2.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface,
                lineHeight = TextUnit(18f, TextUnitType.Sp)
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = career.description ?: "Unknown",
            style = Typography.body2.copy(
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Text(text = buildAnnotatedString {
                appendInlineContent("gradesMatch")
                append(" ${(career.gradesMatch*100).roundToInt()}% Grades")
            }, inlineContent = mapOf("gradesMatch" to InlineTextContent(
                Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.TextCenter)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "",
                    tint = if (career.gradesMatch > 0.49) MaterialTheme.colors.primary else Color.Gray
                )
            }),
                style = Typography.body2.copy(
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colors.onSurface
                ))

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = buildAnnotatedString {
                appendInlineContent("personalityMatch")
                append(" ${(career.personalityMatch*100).roundToInt()}% Personality")
            }, inlineContent = mapOf("personalityMatch" to InlineTextContent(
                Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.TextCenter)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "",
                    tint = if (career.personalityMatch > 0.49) MaterialTheme.colors.primary else Color.Gray
                )
            }),
                style = Typography.body2.copy(
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colors.onSurface
                )
            )
        }
    }
}


@Composable
fun CollegeItem(college: College) {

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Text(text = college.name ?: "Unknown",
            style = Typography.body2.copy(
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colors.onSurface
            )
        )

        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Text(text = buildAnnotatedString {
                appendInlineContent("isAccredited")
                append("Accredited")
            }, inlineContent = mapOf("isAccredited" to InlineTextContent(
                Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.TextCenter)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "",
                    tint = if (college.isAccredited) MaterialTheme.colors.primary else Color.Gray
                )
            }),
                style = Typography.body2.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colors.onSurface
                ))

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = buildAnnotatedString {
                appendInlineContent("isPublic")
                append("Public")
            }, inlineContent = mapOf("isPublic" to InlineTextContent(
                Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.TextCenter)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "",
                    tint = if (college.isPublic) MaterialTheme.colors.primary else Color.Gray,
                    modifier = Modifier.shadow(0.dp)
                )
            }),
                style = Typography.body2.copy(
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colors.onSurface
                )
            )
        }

    }
}