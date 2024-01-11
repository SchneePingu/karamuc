package com.example.karamuc.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.karamuc.ui.theme.KaramucTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BookingDayTabsTopBar(
    days: List<LocalDate>,
    selectedTabIndex: Int,
    onTabSelected: (tabIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (days.isEmpty()) {
        return
    }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier
    ) {
        days.forEachIndexed { tabIndex, day ->
            Tab(
                selected = selectedTabIndex == tabIndex,
                onClick = { onTabSelected(tabIndex) },
                text = { BookingDayTabName(day = day) },
                modifier = modifier
            )
        }
    }
}

@Composable
fun BookingDayTabName(
    day: LocalDate,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.GERMANY),
            modifier = modifier
        )
        Text(
            text = DateTimeFormatter.ofPattern("dd.MM.").format(day),
            modifier = modifier
        )

    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DayTabsTopBarPreview() {
    KaramucTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            BookingDayTabsTopBar(
                listOf(
                    LocalDate.of(1999, 12, 31),
                    LocalDate.of(2000,1, 1)
                ),
                0,
                {},
            )
        }
    }
}