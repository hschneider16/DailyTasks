package com.example.dailytasks.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailytasks.data.SettingsData
import com.example.dailytasks.data.TaskDatabase
import com.example.dailytasks.data.TaskEntity
import com.example.dailytasks.data.toEntity
import com.example.dailytasks.data.toTask
import com.example.dailytasks.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(
    application: Application
) : AndroidViewModel(application) {

    // Dark mode setting function
    private val settingsDataStore =
        SettingsData(application.applicationContext)

    // Checks if dark mode is active
    val isDarkMode: StateFlow<Boolean> =
        settingsDataStore.isDarkMode.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    // Sets dark mode to be enabled
    fun setDarkModeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDarkMode(enabled)
        }
    }

    // Should complete tasks appear in the list?
    val hideCompletedTasks: StateFlow<Boolean> =
        settingsDataStore.isHideCompletedOn.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    // Save preference
    fun setHideCompletedTasks(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setHideCompleted(enabled)
        }
    }

    private val taskDao = TaskDatabase
        .getInstance(application)
        .taskDao()

    val tasks: StateFlow<List<Task>> =
        taskDao.observeTasks()
            .map { rows -> rows.map { it.toTask() } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private val _expandedTaskIds = MutableStateFlow<Set<Long>>(emptySet())
    val expandedTaskIds: StateFlow<Set<Long>> = _expandedTaskIds.asStateFlow()

    // Expand long task descriptions that are otherwise cut off
    fun toggleTaskExpanded(taskId: Long) {
        _expandedTaskIds.update { ids ->
            if (taskId in ids) ids - taskId else ids + taskId
        }
    }

    // Mark tasks as completed
    fun markTaskAsDone(taskId: Long) {
        viewModelScope.launch {
            val existing = tasks.value.firstOrNull { it.id == taskId } ?: return@launch
            taskDao.update(existing.copy(complete=!existing.complete).toEntity())

        }
    }

    // Adds tasks
    fun addTask(
        title: String,
        description: String,
        dueDate: LocalDate
    ) {
        viewModelScope.launch {
            taskDao.insert(
                TaskEntity(
                    title = title,
                    description = description,
                    dueDateEpochDay = dueDate.toEpochDay(),
                    complete = false
                )
            )
        }
    }

    // Edits tasks
    fun editTask(
        taskId: Long,
        title: String,
        description: String,
        dueDate: LocalDate
    ) {
        viewModelScope.launch {
            val existing = tasks.value.firstOrNull { it.id == taskId } ?: return@launch
            taskDao.update(
                existing.copy(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                ).toEntity()
            )
        }
    }

    // Deletes tasks
    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            val existing = tasks.value.firstOrNull { it.id == taskId } ?: return@launch
            taskDao.delete(existing.toEntity())
        }
    }
}


