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
import com.example.dailytasks.ui.AddTaskButton
import com.example.dailytasks.ui.AddTaskDialog
import com.example.dailytasks.ui.HelpScreen
import com.example.dailytasks.ui.SettingsScreen
import com.example.dailytasks.ui.TaskDrawer
import com.example.dailytasks.ui.TaskList
import com.example.dailytasks.ui.TaskTopBar
import kotlinx.coroutines.launch

import com.example.dailytasks.ui.TaskViewModel

enum class TaskScreens (@StringRes val title: Int) {
    Start(title = R.string.app_name),
    HelpScreen(title = R.string.help),
    SettingScreen(title = R.string.settings),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTasksApp(
    viewModel: TaskViewModel,
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showAddTaskDialog by remember {
        mutableStateOf(false)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = TaskScreens.valueOf(
        backStackEntry?.destination?.route
            ?: TaskScreens.Start.name
    )

    fun closeDrawer() {
        scope.launch {
            drawerState.close()
        }
    }

    fun openDrawer() {
        scope.launch {
            drawerState.open()
        }
    }

    BackHandler(
        enabled = drawerState.isOpen
    ) {
        closeDrawer()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            TaskDrawer(
                onCloseClick = ::closeDrawer,
                onSettingsClick = {
                    closeDrawer()
                    navController.navigate(TaskScreens.SettingScreen.name)
                },
                onHelpClick = {
                    closeDrawer()
                    navController.navigate(TaskScreens.HelpScreen.name)
                },
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
            NavHost(
                navController = navController,
                startDestination = TaskScreens.Start.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(TaskScreens.Start.name) {
                    TaskList(
                        tasks = tasks,
                        onTaskClick = viewModel::toggleTaskExpanded,
                        onMarkDoneClick = viewModel::markTaskAsDone
                    )
                }

                composable(TaskScreens.HelpScreen.name) {
                    HelpScreen(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(TaskScreens.SettingScreen.name) {
                    SettingsScreen(
                        isDarkMode = isDarkMode,
                        onDarkModeChanged = viewModel::setDarkModeEnabled,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }

        if (showAddTaskDialog) {
            AddTaskDialog(
                onDismiss = {
                    showAddTaskDialog = false
                },
                onSave = { title, description, dueDate ->
                    viewModel.addTask(
                        title = title,
                        description = description,
                        dueDate = dueDate
                    )

                    showAddTaskDialog = false
                }
            )
        }
    }
}


