package com.amirnlz.stylora.pages.dashboard.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackLanguage
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackModel
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackType
import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToFeedbackScreen: (FeedbackResponse) -> Unit,
    onNavigateToHistory: () -> Unit = {}
) {
    val feedbackModel by viewModel.feedbackModel.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val selectedImageUri = feedbackModel.imageUri
    val context = LocalContext.current

    var feedbackTypeExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.changeImage(it)
                Log.d("photo picker", "Selected URI: $it")
            } ?: Log.d("photo picker", "No media selected")
        }
    )

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collectLatest { feedback ->
            onNavigateToFeedbackScreen(feedback)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Style Feedback") },
                actions = {
                    IconButton(
                        onClick = onNavigateToHistory,
                        enabled = uiState != DashboardUiState.Loading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "View History"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (selectedImageUri == Uri.EMPTY) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Please select an image to start",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    ImageSection(
                        selectedImageUri = selectedImageUri,
                        context = context
                    )
                    DropdownSection(
                        enabled = uiState != DashboardUiState.Loading,
                        feedbackModel = feedbackModel,
                        feedbackTypeExpanded = feedbackTypeExpanded,
                        onFeedbackTypeExpandedChange = { feedbackTypeExpanded = it },
                        languageExpanded = languageExpanded,
                        onLanguageExpandedChange = { languageExpanded = it },
                        onFeedbackTypeSelected = { viewModel.changeFeedbackType(it) },
                        onLanguageSelected = { viewModel.changeLanguage(it) }
                    )
                }
            }

            when (uiState) {
                is DashboardUiState.Loading -> CircularProgressIndicator()
                is DashboardUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = (uiState as DashboardUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is DashboardUiState.Idle -> {}
            }

            ButtonSection(
                enabled = uiState != DashboardUiState.Loading,
                selectedImageUri = selectedImageUri,
                onSelectImage = {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onUploadImage = { viewModel.uploadImage() }
            )
        }
    }
}

@Composable
private fun ImageSection(
    selectedImageUri: Uri?,
    context: Context
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(selectedImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Selected image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = "No image selected",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DropdownSection(
    enabled: Boolean,
    feedbackModel: FeedbackModel,
    feedbackTypeExpanded: Boolean,
    onFeedbackTypeExpandedChange: (Boolean) -> Unit,
    languageExpanded: Boolean,
    onLanguageExpandedChange: (Boolean) -> Unit,
    onFeedbackTypeSelected: (FeedbackType) -> Unit,
    onLanguageSelected: (FeedbackLanguage) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FeedbackTypeDropdown(
            expanded = feedbackTypeExpanded && enabled,
            onExpandedChange = onFeedbackTypeExpandedChange,
            selectedType = feedbackModel.feedbackType,
            onTypeSelected = onFeedbackTypeSelected
        )

        LanguageDropdown(
            expanded = languageExpanded && enabled,
            onExpandedChange = onLanguageExpandedChange,
            selectedLanguage = feedbackModel.language,
            onLanguageSelected = onLanguageSelected
        )
    }
}

@Composable
private fun FeedbackTypeDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedType: FeedbackType,
    onTypeSelected: (FeedbackType) -> Unit
) {
    Box {
        Button(onClick = { onExpandedChange(true) }) {
            Text(selectedType.name)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            FeedbackType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onTypeSelected(type)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun LanguageDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedLanguage: FeedbackLanguage,
    onLanguageSelected: (FeedbackLanguage) -> Unit
) {
    Box {
        Button(onClick = { onExpandedChange(true) }) {
            Text(selectedLanguage.name)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            FeedbackLanguage.entries.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.name) },
                    onClick = {
                        onLanguageSelected(language)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun ButtonSection(
    enabled: Boolean,
    selectedImageUri: Uri?,
    onSelectImage: () -> Unit,
    onUploadImage: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onSelectImage, enabled = enabled) {
            Text(if (selectedImageUri == null) "Select Image" else "Change Image")
        }

        Button(
            onClick = onUploadImage,
            enabled = selectedImageUri != Uri.EMPTY && enabled,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text("Upload to Server")
        }
    }
}