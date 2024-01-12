package com.example.karamuc.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.karamuc.model.DEFAULT_NUMBER_OF_PERSONS
import com.example.karamuc.ui.theme.KaramucTheme

val NUMBER_OF_PERSONS_OPTIONS: List<Int> = listOf(DEFAULT_NUMBER_OF_PERSONS, 3, 4, 5, 6, 7, 10, 11, 12)

@Composable
fun NumberOfPersonsDropDown(
    numberOfPersons: Int?,
    onNumberOfPersonsChange: (numberOfPersons: Int) -> Unit,
    isEnabled: Boolean,
    modifier : Modifier = Modifier

) {
    var expanded by remember { mutableStateOf(false) }

    fun getNumberOfPersons(): Int {
        return numberOfPersons ?: DEFAULT_NUMBER_OF_PERSONS
    }

    fun getDisplayValue(): String {
        return getNumberOfPersons().toString()
    }

    Button(
        onClick = { expanded = true },
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.onPrimary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            modifier = modifier
        )
        Spacer(
            modifier = modifier.size(5.dp)
        )
        Text(
            text = getDisplayValue(),
            modifier = modifier
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
            modifier = modifier
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = modifier
    ) {
        NUMBER_OF_PERSONS_OPTIONS.forEach { numberOfPersons ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = numberOfPersons.toString(),
                        modifier = modifier
                    )
               },
                onClick = {
                    expanded = false
                    onNumberOfPersonsChange(numberOfPersons)
                },
                modifier = modifier
            )
        }
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NumberOfPersonsPickerPreview() {
    KaramucTheme {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            NumberOfPersonsDropDown(
                numberOfPersons = 2,
                onNumberOfPersonsChange = {},
                isEnabled = true
            )
        }
    }
}