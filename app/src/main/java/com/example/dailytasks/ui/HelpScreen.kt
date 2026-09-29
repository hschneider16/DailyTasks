package com.example.dailytasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            HelpSection(
                question = "What is this app?",
                answer = "This app is a daily task tracker or to-do list, and you can use it like you would with a regular, physical to-do list!"
            )

            HelpDivider()

            HelpSection(
                question = "How do I add a task?",
                answer = "To add a task, simply press the plus icon in the bottom right corner of the screen. There, you can name it, give it a date, and write a description for it."
            )

            HelpDivider()

            HelpSection(
                question = "How do I edit a task?",
                answer = "To edit one of your tasks, simply press the edit button, represented with a pencil."
            )
        }
    }
}

@Composable
private fun HelpSection(
    question: String,
    answer: String
) {

    Text(
        // buildAnnotatedString used to make the question bold, and the answer not bold
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(question)
            }

            append(": ")
            append(answer)
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun HelpDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(
            horizontal = 12.dp,
            vertical = 12.dp
        ),
        color = MaterialTheme.colorScheme.surfaceDim
    )
}