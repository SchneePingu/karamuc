package com.example.karamuc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.karamuc.ui.screens.App
import com.example.karamuc.ui.theme.KaramucTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaramucTheme {
                App()
            }
        }
    }
}