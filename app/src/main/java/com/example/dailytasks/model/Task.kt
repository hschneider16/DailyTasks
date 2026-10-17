package com.example.dailytasks.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Task (
    val id: Long,
    var title: String,
    var description: String,
    var dueDate: LocalDate,
    val complete: Boolean = false,
    val isExpanded: Boolean = false
)