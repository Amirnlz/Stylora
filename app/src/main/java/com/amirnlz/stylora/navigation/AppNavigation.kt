package com.amirnlz.stylora.navigation

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.dashboard.ui.DashboardScreen
import com.amirnlz.stylora.pages.dashboard.ui.FeedbackScreen
import kotlinx.serialization.json.Json


@Composable
fun AppNavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Routes.Dashboard,
        modifier = modifier
    ) {
        composable<Routes.Dashboard> {
            DashboardScreen(
                modifier = modifier,
                onNavigateToFeedbackScreen = { feedbackResponse ->
                    Log.d("Navigation", "onNavigateToFeedbackScreen")
                    val feedbackJson = Json.encodeToString(feedbackResponse)
                    navController.navigate(Routes.Feedback(feedbackJson))
                }
            )
        }
        composable<Routes.History> {
            Text("HISTORY")
//            HistoryScreen(navController = navController)
        }
        composable<Routes.Feedback> { backStackEntry ->
            val feedbackJson = backStackEntry.toRoute<Routes.Feedback>().feedbackJson
            val feedbackResponse = Json.decodeFromString<FeedbackResponse>(feedbackJson)
            FeedbackScreen(feedback = feedbackResponse)
        }
    }
}