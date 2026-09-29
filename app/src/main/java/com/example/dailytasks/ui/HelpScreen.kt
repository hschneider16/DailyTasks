package com.example.dailytasks.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dailytasks.R

@Composable
fun HelpScreen(
    onBackClick: () -> Unit
) {
    val helpItems = listOf(
        HelpItem(
            question = stringResource(R.string.q1),
            answer = stringResource(R.string.a1)
        ),
        HelpItem(
            question = stringResource(R.string.q2),
            answer = stringResource(R.string.a2)
        ),
        HelpItem(
            question = stringResource(R.string.q3),
            answer = stringResource(R.string.a3)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            helpItems.forEachIndexed { index, item ->
                HelpSection(
                    question = item.question,
                    answer = item.answer
                )

                if (index < helpItems.lastIndex) {
                    HelpDivider()
                }
            }
        }
    }
}

private data class HelpItem(
    val question: String,
    val answer: String
)


@Composable
private fun HelpSection(
    question: String,
    answer: String
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                expanded = !expanded
            }
            .animateContentSize()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = if (expanded) {
                    "Collapse answer"
                } else {
                    "Expand answer"
                },
                modifier = Modifier.rotate(
                    if (expanded) 90f else 0f
                )
            )
        }

        AnimatedVisibility(
            visible = expanded
        ) {
            Text(
                text = answer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun HelpDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(
            horizontal = 12.dp,
            vertical = 4.dp
        ),
        color = MaterialTheme.colorScheme.surfaceVariant
    )
}