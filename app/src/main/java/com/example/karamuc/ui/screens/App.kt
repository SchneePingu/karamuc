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
import com.example.karamuc.model.AppTopBarViewModel
import com.example.karamuc.model.BookingSlotsViewModel
import com.example.karamuc.model.BookingSlotsViewModelInterface
import com.example.karamuc.model.MockBookingSlotsViewModel
import com.example.karamuc.ui.theme.KaramucTheme
import java.time.LocalDate

@Composable
fun App() {
    val appTopBarViewModel: AppTopBarViewModel = viewModel()
    val bookingSlotsViewModel: BookingSlotsViewModel = viewModel()
    val modifier = Modifier

    AppScreen(
        appTopBarViewModel = appTopBarViewModel,
        bookingSlotsViewModel = bookingSlotsViewModel,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    appTopBarViewModel: AppTopBarViewModel,
    bookingSlotsViewModel: BookingSlotsViewModelInterface,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppTopBar(
                date = appTopBarViewModel.date,
                onDateChange = {
                    appTopBarViewModel.updateDate(it)
                    bookingSlotsViewModel.updateTabIndex(it)
                    bookingSlotsViewModel.updateBookingDays(it)
                    bookingSlotsViewModel.updateBookingSlots()
                },
                numberOfPersons = appTopBarViewModel.numberOfPersons,
                onNumberOfPersonsChange = appTopBarViewModel:: updateNumberOfPersons,
                isLoading = bookingSlotsViewModel.isLoading(),
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
            if (appTopBarViewModel.date != null) {
                BookingSlotsScreen(
                    days = bookingSlotsViewModel.bookingDays,
                    tabIndex = bookingSlotsViewModel.tabIndex,
                    onTabIndexChange = bookingSlotsViewModel::updateTabIndex,
                    bookingSlots = bookingSlotsViewModel.getBookingSlots(),
                    numberOfPersons = appTopBarViewModel.numberOfPersons,
                    onError = bookingSlotsViewModel::updateBookingSlots,
                    modifier = modifier
                )
            } else {
                InfoScreen()
            }
        }
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppScreenPreview() {
    KaramucTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val appTopBarViewModel = AppTopBarViewModel()
            appTopBarViewModel.date = LocalDate.of(1999, 12, 31)

            val mockBookingSlotsViewModel = MockBookingSlotsViewModel()

            AppScreen(
                appTopBarViewModel = appTopBarViewModel,
                bookingSlotsViewModel = mockBookingSlotsViewModel
            )
        }
    }
}