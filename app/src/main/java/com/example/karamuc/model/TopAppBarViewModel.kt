package com.example.karamuc.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate

const val DEFAULT_NUMBER_OF_PERSONS: Int = 2

class TopAppBarViewModel : ViewModel() {
    var date: LocalDate? by mutableStateOf(LocalDate.now())
    var numberOfPersons: Int? by mutableStateOf(DEFAULT_NUMBER_OF_PERSONS)

    init {
        date = null
    }

    fun updateDate(newDate: LocalDate) {
        date = newDate
    }

    fun updateNumberOfPersons(newNumberOfPersons: Int) {
        numberOfPersons = newNumberOfPersons
    }
}