package com.example.skypulse.view.events

import androidx.annotation.StringRes

sealed interface HomeUiEvent {
    data class ShowSnackbar(
        @StringRes val messageRes: Int
    ) : HomeUiEvent
}