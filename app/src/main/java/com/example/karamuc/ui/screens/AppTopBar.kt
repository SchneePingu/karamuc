package com.example.karamuc.ui.screens

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.karamuc.ui.theme.KaramucTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    date: LocalDate?,
    onDateChange: (date: LocalDate) -> Unit,
    numberOfPersons: Int?,
    onNumberOfPersonsChange: (numberOfPersons: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors =  TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        actions = {
                  NumberOfPersonsDropDown(
                      numberOfPersons = numberOfPersons,
                      onNumberOfPersonsChange = onNumberOfPersonsChange,
                      isEnabled = date != null,
                      modifier = modifier
                  )
        },
        title = {
                BookingDatePicker(
                    date = date,
                    onDateChange = onDateChange,
                    modifier = modifier
                )
        },
        modifier = modifier
    )
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppTopBarPreview() {
    KaramucTheme {
        AppTopBar(null, {}, null, {} )
    }
}