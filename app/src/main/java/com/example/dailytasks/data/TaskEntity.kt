package com.example.dailytasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dailytasks.model.Task
import java.time.LocalDate

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val dueDateEpochDay: Long,
    val complete: Boolean
)

fun TaskEntity.toTask() = Task(
    id = id,
    title = title,
    description = description,
    dueDate = LocalDate.ofEpochDay(dueDateEpochDay),
    complete = complete
)

fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    dueDateEpochDay = dueDate.toEpochDay(),
    complete = complete
)