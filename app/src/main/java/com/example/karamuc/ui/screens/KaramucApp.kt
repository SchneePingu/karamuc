package com.example.karamuc.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.karamuc.model.BookingViewModel
import com.example.karamuc.model.TopAppBarViewModel
import com.example.karamuc.service.BookingDaysService
import com.example.karamuc.ui.theme.KaramucTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaramucApp() {
    val topAppBarViewModel: TopAppBarViewModel = viewModel()
    val test: BookingViewModel = viewModel()
    val modifier = Modifier

    Scaffold(
        topBar = {
            KaramucTopAppBar(
                date = topAppBarViewModel.date,
                onDateChange = {
                    topAppBarViewModel.updateDate(it)
                    test.updateTabIndex(it)
                    test.updateState(it)
                },
                numberOfPersons = topAppBarViewModel.numberOfPersons,
                onNumberOfPersonsChange = topAppBarViewModel:: updateNumberOfPersons,
                modifier = modifier
            )
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            if (topAppBarViewModel.date != null) {
                BookingDayTabs(
                    days = BookingDaysService.getBookingWeek(topAppBarViewModel.date),
                    tabIndex = test.tabIndex,
                    onTabIndexChange = test::updateTabIndex,
                    bookingDays = listOf(test.wednesday, test.thursday, test.friday, test.saturday, test.sunday),
                    numberOfPersons = topAppBarViewModel.numberOfPersons,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KaramucAppPreview() {
    KaramucTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            KaramucApp()
        }
    }
}