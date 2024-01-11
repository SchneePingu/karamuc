package com.example.karamuc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.karamuc.model.BookingUiState
import com.example.karamuc.model.BookingViewModel
import com.example.karamuc.ui.screens.KaramucApp
import com.example.karamuc.ui.screens.KaramucTopAppBar
import com.example.karamuc.ui.theme.KaramucTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaramucTheme {
                KaramucApp()
            }
        }
    }
}