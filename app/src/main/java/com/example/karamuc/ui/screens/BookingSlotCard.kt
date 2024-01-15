package com.example.karamuc.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.karamuc.model.BookingSlot
import com.example.karamuc.model.getPrice
import com.example.karamuc.model.getRoomName
import com.example.karamuc.model.getTimeSlot
import com.example.karamuc.ui.theme.KaramucTheme
import java.time.LocalTime

@Composable
fun BookingSlotCard(
    bookingSlot: BookingSlot,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = modifier.size(10.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

            ) {
            Text(
                text = getTimeSlot(bookingSlot),
                textAlign = TextAlign.Center,
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Text(
                text = getPrice(bookingSlot),
                modifier = modifier
                    .weight(0.5f)
                    .fillMaxWidth()
            )
            Text(
                text = getRoomName(bookingSlot),
                textAlign = TextAlign.Center,
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = modifier.size(10.dp))
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookingSlotCardViewPreview() {
    KaramucTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            BookingSlotCard(
                BookingSlot(
                    fromTime = LocalTime.of(18,30,0),
                    toTime = LocalTime.of(20, 30, 0),
                    price = 100,
                    roomName = "Cat Café",
                    isAvailable = true,
                    roomSize = 2,
                    maxRoomSize = 2,
                )
            )
        }
    }
}