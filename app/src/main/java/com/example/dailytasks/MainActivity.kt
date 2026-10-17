package com.example.dailytasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailytasks.ui.TaskViewModel
import com.example.dailytasks.ui.theme.DailyTasksTheme

/* Daily Tasks App, or Daily Task Tracker:
* This app is a to-do list where users can create new tasks,
* set a title, description, and due date for their tasks,
* mark them as done, edit them, or delete them.
* Programmed by Hunter Schneider
* SDEV 264
* Prof. Michael Feagans
* Thank you to Daniel Martinus for the Konfetti library:
* https://github.com/DanielMartinus/Konfetti */
class MainActivity : ComponentActivity() {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by taskViewModel.isDarkMode
                .collectAsStateWithLifecycle()

            DailyTasksTheme (
                darkTheme = isDarkMode
            ) {
                DailyTasksApp(
                    viewModel = taskViewModel,
                )
                }
            }
        }
    }

