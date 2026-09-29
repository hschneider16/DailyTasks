package com.example.dailytasks.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dailytasks.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Displays read-only text field for selecting a task due date
fun DueDateField(
    dueDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("MMM d, yyyy")
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = dueDate.format(dateFormatter),
            onValueChange = { },
            label = { Text(stringResource(R.string.due_date)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker = true
                }
        )

        if (showDatePicker) {
            val initialDateMillis = dueDate
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()

            // Holds the date currently selected inside the date picker
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialDateMillis
            )

            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                // Confirm selection button
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = Instant
                                    .ofEpochMilli(millis)
                                    .atZone(ZoneOffset.UTC)
                                    .toLocalDate()

                                onDateSelected(selectedDate)
                            }

                            // Close dialog after confirmation
                            showDatePicker = false
                        }
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                },
                // Cancel button, closes dialog without changing date
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                // The actual calendar UI displayed in the dialog
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }
}