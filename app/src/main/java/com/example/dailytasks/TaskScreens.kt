package com.example.dailytasks

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.dailytasks.model.Task
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dailytasks.ui.TaskViewModel

enum class TaskScreen (@StringRes val title: Int) {
    Start(title = R.string.app_name),
    HelpScreen(title = R.string.help),
    SettingScreen(title = R.string.settings),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTasksApp(
    viewModel: TaskViewModel
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = TaskScreen.valueOf(
        backStackEntry?.destination?.route ?: TaskScreen.Start.name
    )

    var showAddTaskDialog by remember { mutableStateOf(false) }

    // Logic handling hamburger menu closing
    fun closeDrawer() {
        scope.launch {
            drawerState.close()
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
            // Hamburger menu drawer
            TaskNav(
                onCloseClick = ::closeDrawer,
                onSettingsClick = {
                    closeDrawer()
                    navController.navigate(TaskScreen.SettingScreen.name)
                },
                onHelpClick = {
                    closeDrawer()
                    navController.navigate(TaskScreen.HelpScreen.name)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    // App headline
                    title = {
                        Text(
                            text = stringResource(currentScreen.title),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    // Hamburger menu
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.menu_desc)
                            )
                        }
                    }
                )
            },
            // Add task button
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        showAddTaskDialog = true
                    },
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
        ) { innerPadding ->
            TaskList(
                tasks = tasks,
                onTaskClick = viewModel::toggleTaskExpanded,
                onMarkDoneClick = viewModel::markTaskAsDone,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }

    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = {
                showAddTaskDialog = false
            },
            // Save the task and add it to the list
            onSave = { title: String, description: String, dueDate: LocalDate ->
                viewModel.addTask(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                )
                showAddTaskDialog = false
            }
        )
    }
}

@Composable
private fun TaskList(
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
private fun TaskCard(
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

@Composable
private fun TaskNav(
    onCloseClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    // App's hamburger navigation menu
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back arrow
                IconButton(
                    onClick = onCloseClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.close_desc)
                    )
                }
// "Menu" headline at top of hamburger menu
                Text(
                    text = stringResource(R.string.menu),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )
// Settings button in hamburger menu
            NavItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                },
                label = stringResource(R.string.settings),
                onClick = onSettingsClick
                // TODO: settings function
            )
//  Help button in hamburger menu
            NavItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = null
                    )
                },
                label = stringResource(R.string.help),
                onClick = onHelpClick
                // TODO: help function
            )
        }
    }
}

@Composable
// Style for items in the hamburger menu
private fun NavItem(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun AddTaskDialog(
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        description: String,
        dueDate: LocalDate
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Task creation menu
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.add_task))
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(stringResource(R.string.title))
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(stringResource(R.string.desc))
                    },
                    minLines = 3
                )
            }
        },
        // Button to confirm task creation
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            title.trim(),
                            description.trim(),
                            LocalDate.now()
                        )
                    }
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        // Button to cancel creation
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
