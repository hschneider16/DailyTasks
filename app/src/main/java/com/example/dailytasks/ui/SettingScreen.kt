package com.example.dailytasks.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dailytasks.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    isHideCompletedOn: Boolean,
    onHideCompletedChanged: (Boolean) -> Unit
) {
    // Setting menu UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            DarkModeSetting(
                isDarkMode = isDarkMode,
                onDarkModeChanged = onDarkModeChanged
            )
            SettingDivider()
            HideCompleteSetting(
                hideCompletedTasks = isHideCompletedOn,
                onHideCompletedChanged = onHideCompletedChanged
            )
        }
    }

@Composable
// Creates the UI for the dark mode setting's toggle & toggle logic
fun DarkModeSetting(
    isDarkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Setting title
            Text(
                text = stringResource(R.string.darkmode),
                fontWeight = FontWeight.Bold
            )

            // Setting description
            Text(
                text = stringResource(R.string.darkmode_desc)
            )
        }

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        // The actual toggle switch
        Switch(
            checked = isDarkMode,
            onCheckedChange = onDarkModeChanged,
            modifier = Modifier,
            thumbContent = null
        )
    }
}

@Composable
// Creates the UI for the show completed tasks on/off setting's toggle & toggle logic
fun HideCompleteSetting(
    hideCompletedTasks: Boolean,
    onHideCompletedChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Setting title
            Text(
                text = stringResource(R.string.hidecomplete),
                fontWeight = FontWeight.Bold
            )

            // Setting description
            Text(
                text = stringResource(R.string.hidecomplete_desc)
            )
        }

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        // The actual toggle switch
        Switch(
            checked = hideCompletedTasks,
            onCheckedChange = onHideCompletedChanged,
            modifier = Modifier,
            thumbContent = null
        )
    }
}

@Composable
private fun SettingDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(
            horizontal = 12.dp,
            vertical = 12.dp
        ),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}