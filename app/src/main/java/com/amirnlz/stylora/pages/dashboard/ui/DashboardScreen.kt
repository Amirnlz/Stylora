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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToFeedbackScreen: (FeedbackResponse) -> Unit,
) {
    val userSelection by viewModel.userSelection.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val selectedImageUri = userSelection.imageUri
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
        Log.d("Navigation", "onNavigateToFeedbackScreen - 1 ")
        viewModel.navigationEvents.collect { feedback ->
            Log.d("Navigation", "onNavigateToFeedbackScreen - 2")

            onNavigateToFeedbackScreen(feedback)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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

            if (selectedImageUri != null) {
                DropdownSection(
                    userSelection = userSelection,
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
            is DashboardUiState.Loading -> Text("LOADING...")
            is DashboardUiState.Error -> Text((uiState as DashboardUiState.Error).message)
            is DashboardUiState.Idle -> {} // No additional UI for Idle
        }


        ButtonSection(
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
    userSelection: UserSelectionModel,
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
            expanded = feedbackTypeExpanded,
            onExpandedChange = onFeedbackTypeExpandedChange,
            selectedType = userSelection.feedbackType,
            onTypeSelected = onFeedbackTypeSelected
        )

        LanguageDropdown(
            expanded = languageExpanded,
            onExpandedChange = onLanguageExpandedChange,
            selectedLanguage = userSelection.language,
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
    selectedImageUri: Uri?,
    onSelectImage: () -> Unit,
    onUploadImage: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onSelectImage) {
            Text(if (selectedImageUri == null) "Select Image" else "Change Image")
        }

        Button(
            onClick = onUploadImage,
            enabled = selectedImageUri != null,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text("Upload to Server")
        }
    }
}