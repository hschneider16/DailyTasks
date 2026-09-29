package com.example.dailytasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.dailytasks.R
import com.example.dailytasks.model.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (Long) -> Unit,
    onMarkDoneClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Scrollable column for viewing tasks
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = tasks,
            key = { task -> task.id }
        ) { task ->
            TaskCard(
                task = task,
                // Toggle expansion
                onTaskClick = {
                    onTaskClick(task.id)
                },
                // Mark as completed
                onMarkDoneClick = {
                    onMarkDoneClick(task.id)
                }
                //TODO: Edit functionality
            )
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onTaskClick: () -> Unit,
    onMarkDoneClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Name of task
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            // Task due date
            Text(
                text = "Due: ${task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        // Description text
        Text(
            text = task.description,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onTaskClick),
            maxLines = if (task.isExpanded) Int.MAX_VALUE else 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
// Function to cut off long text when not expanded
        if (!task.isExpanded && task.description.length > 90) {
            Text(
                text = stringResource(R.string.more),
                modifier = Modifier
                    .clickable(onClick = onTaskClick)
                    .padding(top = 2.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // The edit button
            Button(
                onClick = {
                    // TODO: add edit function
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(stringResource(R.string.edit_task))
            }
            // The mark as done button
            Button(
                onClick = onMarkDoneClick
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = if (task.complete) {
                        stringResource(R.string.completed)
                    } else {
                        stringResource(R.string.mark_done)
                    }
                )
            }
        }
    }
}