package com.example.karamuc.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.karamuc.R
import com.example.karamuc.ui.theme.KaramucTheme

@Composable
fun InfoScreen(
    modifier: Modifier = Modifier

) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxHeight()
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
        )
        Spacer(
            modifier = modifier.size(20.dp)
        )
        Row(
            modifier = modifier
        ) {
            Text(
                text = "1.",
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.weight(0.2f)
            )
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.weight(0.1f)
            )
            Text(
                text = stringResource(R.string.info_choose_date),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.weight(0.6f)
            )
        }
        Spacer(
            modifier = modifier.size(10.dp)
        )
        Row(
            modifier = modifier
        ) {
            Text(
                text = "2.",
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.weight(0.2f)
            )
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.weight(0.1f)
            )
            Text(
                text = stringResource(R.string.info_choose_number_of_persons),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.weight(0.6f)
            )
        }
        Spacer(
            modifier = modifier.size(10.dp)
        )
        Row(
            modifier = modifier
        ) {
            Text(
                text = "3.",
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.weight(0.2f)
            )
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.weight(0.1f)
            )
            Text(
                text = stringResource(R.string.info_easter_egg),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.weight(0.6f)
            )
        }
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun InfoScreenPreview() {
    KaramucTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            InfoScreen()
        }
    }
}
