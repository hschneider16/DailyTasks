package com.example.dailytasks.ui

import androidx.lifecycle.ViewModel
import com.example.dailytasks.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class TaskViewModel : ViewModel() {

    // Dark mode setting function
    val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun setDarkModeEnabled(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }
    private val _tasks = MutableStateFlow(
        listOf(
            Task(
                id = 1,
                title = "Introduction Task",
                description = "Welcome to the Daily Task Tracker! The plus button in the bottom right hand corner can add tasks to a list that will show here. To change your settings, click on the three lines in the top left corner. A help page is also available if you need guidance on what to do. Have fun!",
                dueDate = LocalDate.now()
            )
        )
    )

    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    // Expand long task descriptions that are otherwise cut off
    fun toggleTaskExpanded(taskId: Long) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                task.copy(isExpanded = !task.isExpanded)
            } else {
                task
            }
        }
    }

    // Mark tasks as completed
    fun markTaskAsDone(taskId: Long) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                task.copy(complete = !task.complete)
            } else {
                task
            }
        }
    }

    // Adds tasks
    fun addTask(
        title: String,
        description: String,
        dueDate: LocalDate
    ) {
        val newTask = Task(
            id = System.currentTimeMillis(),
            title = title,
            description = description,
            dueDate = dueDate
        )

        _tasks.value = _tasks.value + newTask
    }

    // Edits tasks
    fun editTask(
        taskId: Long,
        title: String,
        description: String,
        dueDate: LocalDate
    ) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                task.copy(
                    title = title,
                    description = description,
                    dueDate = dueDate
                )
            } else {
                task
            }
        }
    }

    // Deletes tasks
    fun deleteTask(taskId: Long) {
        _tasks.value = _tasks.value.filterNot { it.id == taskId }
        // TODO: add function
    }
}

