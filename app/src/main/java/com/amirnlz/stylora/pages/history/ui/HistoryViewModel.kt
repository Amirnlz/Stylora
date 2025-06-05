package com.amirnlz.stylora.pages.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirnlz.stylora.pages.history.domain.use_cases.GetFeedbackHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getFeedbackHistoryUseCase: GetFeedbackHistoryUseCase
) : ViewModel() {
    private var _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()


    fun getFeedbackHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = HistoryUiState.Loading
            val result = getFeedbackHistoryUseCase.invoke()
            result.onSuccess { items ->
                _uiState.value = HistoryUiState.Success(items)
            }.onFailure { exception ->
                _uiState.value = HistoryUiState.Error(exception.message ?: "Failed to load history")
            }

        }

    }

}