package com.amirnlz.stylora.pages.dashboard.ui


sealed class DashboardUiState {
    object Idle : DashboardUiState()
    object Loading : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

