package com.example.karamuc.ui.screens

import android.app.DatePickerDialog
import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.karamuc.R
import com.example.karamuc.ui.theme.KaramucTheme
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun BookingDatePicker(
    date: LocalDate?,
    onDateChange: (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val today = LocalDate.now()
    
    fun getDate(): LocalDate {
        return date ?: today
    }

    fun getDateDisplayValue(placeholder: String): String {
        if (date == null) {
            return placeholder
        }

        return DateTimeFormatter
            .ofPattern("dd.MM.yyyy")
            .format(getDate())
    }

    Button(
        onClick = {
            val dialog = DatePickerDialog(
                context,
//                R.style.KaramucDatePicker,
                { _, year, month, day ->
                    onDateChange(LocalDate.of(year, month + 1, day))
                },
                getDate().year,
                getDate().monthValue - 1,
                getDate().dayOfMonth,
            )

            dialog.datePicker.minDate = today
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

            dialog.show()
        },
        colors = ButtonDefaults.buttonColors(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.onPrimary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.DateRange,
            contentDescription = null,
            modifier = modifier
        )
        Spacer(
            modifier = modifier.size(5.dp)
        )
        Text(
            text = getDateDisplayValue(stringResource(R.string.date_button)),
            fontSize = integerResource(R.integer.date_picker_font_size).sp,
            modifier = modifier
        )
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookingDatePickerPreview() {
    KaramucTheme {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            BookingDatePicker(
                date = null,
                onDateChange = {}
            )
        }
    }
}