package com.example.karamuc.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.karamuc.model.BookingSlot
import com.example.karamuc.ui.theme.KaramucTheme
import java.time.LocalTime

@Composable
fun BookingSlotsList(
    bookingSlots: List<BookingSlot>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(bookingSlots) {
            Box(
                modifier = modifier.fillMaxWidth(0.95f)
            ) {
                Column(
                    modifier = modifier
                ) {
                    Spacer(
                        modifier = modifier.size(10.dp)
                    )
                    BookingSlotCard(
                        bookingSlot = it,
                        modifier = modifier
                    )
                    Spacer(
                        modifier = modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookingSlotsViewPreview() {
    KaramucTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            BookingSlotsList(
                listOf(
                    BookingSlot(
                        LocalTime.of(18,30,0),
                        LocalTime.of(20, 30, 0),
                        100,
                        "Cat Café",
                        isAvailable = true,
                        persons = 2,
                        roomSize = 2
                    ),
                    BookingSlot(
                        LocalTime.of(18,30,0),
                        LocalTime.of(20, 30, 0),
                        100,
                        "Godzilla",
                        isAvailable = true,
                        persons = 2,
                        roomSize = 2
                    )
                )
            )
        }
    }
}