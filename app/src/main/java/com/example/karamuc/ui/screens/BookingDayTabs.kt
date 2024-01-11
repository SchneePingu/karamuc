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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.karamuc.model.BookingUiState
import com.example.karamuc.service.BookingDaysService
import com.example.karamuc.ui.theme.KaramucTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookingDayTabs(
    days: List<LocalDate>,
    tabIndex: Int,
    onTabIndexChange: (tabIndex: Int) -> Unit,
    bookingDays: List<BookingUiState>,
    numberOfPersons: Int?,
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
            BookingDayTabsTopBar(
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
            when(bookingDays[pageIndex]) {
                is BookingUiState.Loading -> Text("Loading")
                is BookingUiState.Error -> Text("Error")
                is BookingUiState.Success ->
            BookingSlotsList(
                bookingSlots = BookingDaysService.filterSize(
                    (bookingDays[pageIndex] as BookingUiState.Success).slots,
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
            BookingDayTabs(
                days = listOf(
                    LocalDate.of(1999, 12, 30),
                    LocalDate.of(1999, 12, 31),
                ),
                tabIndex = 0,
                onTabIndexChange = {},
                bookingDays = listOf(
                    BookingUiState.Loading,
                    BookingUiState.Error,
                ),
                numberOfPersons = 2
            )
        }
    }
}

