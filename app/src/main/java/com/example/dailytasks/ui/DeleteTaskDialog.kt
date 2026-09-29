package com.example.dailytasks.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.dailytasks.R

// This is the UI element that pops up when the delete task button is clicked
@Composable
fun DeleteTaskDialog(
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.delete_task))
        },
        text = {
            Text(
                stringResource(
                    R.string.delete_confirm,
                )
            )
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDelete()
                }
            ) {
                Text(stringResource(R.string.delete))
            }
        }
    )
}