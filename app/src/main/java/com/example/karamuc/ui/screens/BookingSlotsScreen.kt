package com.example.karamuc.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.karamuc.model.BookingSlotsUiState
import com.example.karamuc.service.BookingSlotService
import com.example.karamuc.ui.theme.KaramucTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookingSlotsScreen(
    days: List<LocalDate>,
    tabIndex: Int,
    onTabIndexChange: (tabIndex: Int) -> Unit,
    bookingSlots: List<BookingSlotsUiState>,
    numberOfPersons: Int?,
    onError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(initialPage = tabIndex)

    LaunchedEffect(tabIndex) {
        pagerState.scrollToPage(tabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onTabIndexChange(pagerState.currentPage)
    }

    Scaffold(
        topBar = {
            BookingSlotsScreenTopBar(
                days = days,
                selectedTabIndex = tabIndex,
                onTabSelected = onTabIndexChange,
            )
        },
        modifier = modifier
    ) {
        HorizontalPager(
            pageCount = days.size,
            state = pagerState,
            modifier = modifier.padding(it)
        ) { pageIndex ->
            when(bookingSlots[pageIndex]) {
                is BookingSlotsUiState.Loading -> LoadingScreen(
                    modifier = modifier
                )
                is BookingSlotsUiState.Error -> ErrorScreen(
                    onError = onError,
                    modifier = modifier
                )
                is BookingSlotsUiState.Success -> BookingSlotsList(
                    bookingSlots = BookingSlotService.filterByNumberOfPersons(
                        (bookingSlots[pageIndex] as BookingSlotsUiState.Success).slots,
                        numberOfPersons
                    ),
                    modifier = modifier
                )
            }
        }
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookingDayTabViewPreview() {
    KaramucTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            BookingSlotsScreen(
                days = listOf(
                    LocalDate.of(1999, 12, 30),
                    LocalDate.of(1999, 12, 31),
                ),
                tabIndex = 0,
                onTabIndexChange = {},
                bookingSlots = listOf(
                    BookingSlotsUiState.Error,
                    BookingSlotsUiState.Error,
                ),
                numberOfPersons = 2,
                onError = {}
            )
        }
    }
}

