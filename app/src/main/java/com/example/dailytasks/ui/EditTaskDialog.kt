package com.example.dailytasks.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dailytasks.R
import com.example.dailytasks.model.Task
import java.time.LocalDate

// This is the UI element that pops up when the edit task button is clicked
@Composable
fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        description: String,
        dueDate: LocalDate
    ) -> Unit
) {
    var title by remember(task.id) {
        mutableStateOf(task.title)
    }

    var description by remember(task.id) {
        mutableStateOf(task.description)
    }

    var dueDate by remember(task.id) {
        mutableStateOf(task.dueDate)
    }
// Edit dialog
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.edit))
        },
        text = {
            Column {
                // Title field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        (stringResource(R.string.title))
                    }
                )
                // Description field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = {
                        (stringResource(R.string.desc))
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DueDateField(
                    dueDate = dueDate,
                    onDateSelected = {
                        dueDate = it
                    }
                )
            }
        },
        // Save edits to the task
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        title,
                        description,
                        dueDate
                    )
                },
                enabled = title.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        },
        // Cancel edit
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}