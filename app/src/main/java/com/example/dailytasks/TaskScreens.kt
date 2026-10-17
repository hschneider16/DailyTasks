package com.example.dailytasks

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dailytasks.model.Task
import com.example.dailytasks.ui.AddTaskButton
import com.example.dailytasks.ui.AddTaskDialog
import com.example.dailytasks.ui.DeleteTaskDialog
import com.example.dailytasks.ui.EditTaskDialog
import com.example.dailytasks.ui.HelpScreen
import com.example.dailytasks.ui.SettingsScreen
import com.example.dailytasks.ui.TaskDrawer
import com.example.dailytasks.ui.TaskList
import com.example.dailytasks.ui.TaskTopBar
import com.example.dailytasks.ui.TaskViewModel
import kotlinx.coroutines.launch

enum class TaskScreens (@StringRes val title: Int) {
    Start(title = R.string.app_name),
    HelpScreen(title = R.string.help),
    SettingScreen(title = R.string.settings),
}

// Main composable for the Daily Tasks app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTasksApp(
    viewModel: TaskViewModel
) {
    val tasks: List<Task> by viewModel.tasks.collectAsStateWithLifecycle()
    val expandedTaskIds by viewModel.expandedTaskIds.collectAsStateWithLifecycle()
    val isDarkMode: Boolean by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val hideCompletedTasks by viewModel.hideCompletedTasks
        .collectAsStateWithLifecycle()

    val visibleTasks = if (hideCompletedTasks) {
        tasks.filterNot { it.complete }
    } else {
        tasks
    }

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var editingTask by remember { mutableStateOf<Task?>(null) }
    var deletingTask by remember { mutableStateOf<Task?>(null) }
    var showAddTaskDialog by remember {
        mutableStateOf(false)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()

    // Gets the route of the current screen
    val currentRoute: String =
        backStackEntry?.destination?.route
            ?: TaskScreens.Start.name

    // Converts the current route name into a TaskScreens enum value
    val currentScreen: TaskScreens =
        TaskScreens.entries.firstOrNull { screen ->
            screen.name == currentRoute
        } ?: TaskScreens.Start

    // Closes the navigation drawer
    fun closeDrawer() {
        scope.launch {
            drawerState.close()
        }
    }

    // Opens the navigation drawer
    fun openDrawer() {
        scope.launch {
            drawerState.open()
        }
    }

    // If the drawer is open, pressing the system Back button closes it
    BackHandler(
        enabled = drawerState.isOpen
    ) {
        closeDrawer()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            TaskDrawer(
                // Close the drawer when the close button is clicked
                onCloseClick = ::closeDrawer,

                // Navigate to the settings screen
                onSettingsClick = {
                    closeDrawer()
                    navController.navigate(TaskScreens.SettingScreen.name)
                },

                // Navigate to the help screen
                onHelpClick = {
                    closeDrawer()
                    navController.navigate(TaskScreens.HelpScreen.name)
                },

                // Navigate to the task (main) screen
                onTaskClick = {
                    closeDrawer()
                    navController.navigate(TaskScreens.Start.name)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TaskTopBar(
                    title = currentScreen.title,
                    onMenuClick = ::openDrawer
                )
            },
            floatingActionButton = {
                AddTaskButton(
                    onClick = {
                        showAddTaskDialog = true
                    }
                )
            }
        ) { innerPadding ->

            // The app's navigation graph
            NavHost(
                navController = navController,
                startDestination = TaskScreens.Start.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Task list screen
                composable(TaskScreens.Start.name) {
                    TaskList(
                        tasks = visibleTasks,
                        expandedTaskIds = expandedTaskIds,
                        onTaskClick = viewModel::toggleTaskExpanded,
                        onMarkDoneClick = viewModel::markTaskAsDone,
                        onEditClick = { task ->
                            editingTask = task
                        },
                        onDeleteClick = { task ->
                            deletingTask = task
                        }
                    )
                }

                // Help screen
                composable(TaskScreens.HelpScreen.name) {
                    HelpScreen(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                // Settings screen
                composable(TaskScreens.SettingScreen.name) {
                    SettingsScreen(
                        isDarkMode = isDarkMode,
                        onDarkModeChanged = viewModel::setDarkModeEnabled,
                        isHideCompletedOn = hideCompletedTasks,
                        onHideCompletedChanged = viewModel::setHideCompletedTasks,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }

        // Show add task dialog if button pressed
        if (showAddTaskDialog) {
            AddTaskDialog(
                // Hide the dialog without saving
                onDismiss = {
                    showAddTaskDialog = false
                },

                // Add the new task
                onSave = { title, description, dueDate ->
                    viewModel.addTask(
                        title = title,
                        description = description,
                        dueDate = dueDate
                    )

                    // Hide the dialog after saving
                    showAddTaskDialog = false
                }
            )
        }
        // Edit task dialog
        editingTask?.let { task ->
            EditTaskDialog(
                task = task,
                // Cancel selection and close
                onDismiss = {
                    editingTask = null
                },
                // Edit selection and save
                onSave = { title, description, dueDate ->
                    viewModel.editTask(
                        taskId = task.id,
                        title = title,
                        description = description,
                        dueDate = dueDate
                    )

                    editingTask = null
                }
            )
        }

        // Delete task dialog
        deletingTask?.let { task ->
            DeleteTaskDialog(
                // Cancel selection and close
                onDismiss = {
                    deletingTask = null
                },
                // Delete selected task
                onDelete = {
                    viewModel.deleteTask(
                        taskId = task.id,
                    )
                    deletingTask = null
                }
            )
        }
    }
}