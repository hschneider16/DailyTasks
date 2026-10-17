package com.example.dailytasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.dailytasks.R
import com.example.dailytasks.model.Task
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.rememberKonfettiState
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Composable
fun TaskList(
    tasks: List<Task>,
    expandedTaskIds: Set<Long>,
    onTaskClick: (Long) -> Unit,
    onMarkDoneClick: (Long) -> Unit,
    onEditClick: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    // Confetti state for when a task is marked complete :)
    val konfettiState = rememberKonfettiState()


    val party = Party(
        position = Position.Relative(0.5, 0.3),
        emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(100)
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Scrollable column for viewing tasks
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = tasks,
                key = { task -> task.id }
            ) { task ->
                TaskCard(
                    task = task,
                    isExpanded = task.id in expandedTaskIds,
                    // Toggle expansion
                    onTaskClick = {
                        onTaskClick(task.id)
                    },
                    // Mark as completed
                    onMarkDoneClick = {
                        if (!task.complete) {
                            onMarkDoneClick(task.id)
                            // Show confetti :)
                            konfettiState.start(party)
                        } else {
                            onMarkDoneClick(task.id)
                        }
                    },
                    // Edit task
                    onEditClick = {
                        onEditClick(task)
                    },
                    // Delete task
                    onDeleteClick = {
                        onDeleteClick(task)
                    }
                )
            }
        }
        KonfettiView(
            state = konfettiState,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
// UI element for what each task on the list should look like
fun TaskCard(
    task: Task,
    isExpanded: Boolean,
    onTaskClick: () -> Unit,
    onMarkDoneClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val isOverdue =
        !task.complete && LocalDate.now().isAfter(task.dueDate)

    val dueDateColor =
        if (isOverdue) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }

    // Logic for checking if task is complete, and changing the task color to be duller if true
    val isComplete =
        task.complete

    val isCompleteColor =
        if (isComplete) {
            MaterialTheme.colorScheme.outlineVariant
        } else {
            MaterialTheme.colorScheme.surface
        }

    val isCompleteTextColor =
        if (isComplete) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(isCompleteColor)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            // Name of task
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = (isCompleteTextColor)
            )
            // Task due date
            Text(
                // Checks if the task is overdue and makes it red to draw attention!
                text = if (isOverdue) {
                    "Overdue: ${task.dueDate.format(
                        DateTimeFormatter.ofPattern("MMM d, yyyy")
                    )}"
                } else {
                    "Due: ${task.dueDate.format(
                        DateTimeFormatter.ofPattern("MMM d, yyyy")
                    )}"
                },
                style = MaterialTheme.typography.labelLarge,
                color = dueDateColor
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        // Description text
            Text(
                text = task.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onTaskClick),
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = (isCompleteTextColor)
            )
// Function to cut off long text when not expanded
            if (!isExpanded && task.description.length > 90) {
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
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp)
        ) {
            // The delete button
            Button(
                onClick = onDeleteClick,
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = stringResource(R.string.delete_task)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // The edit button
            Button(
                onClick = onEditClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_task)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(stringResource(R.string.edit))
            }
            Spacer(modifier = Modifier.width(8.dp))
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

@Composable
// Button for adding a task in the bottom right corner of the screen
fun AddTaskButton(
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        },
        text = {
            Text(stringResource(R.string.add_task))
        }
    )
}