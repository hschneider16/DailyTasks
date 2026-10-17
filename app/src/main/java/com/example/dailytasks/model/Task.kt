package com.example.dailytasks.model

import java.time.LocalDate

data class Task (
    val id: Long,
    var title: String,
    var description: String,
    var dueDate: LocalDate,
    val complete: Boolean = false,
    val isExpanded: Boolean = false
)