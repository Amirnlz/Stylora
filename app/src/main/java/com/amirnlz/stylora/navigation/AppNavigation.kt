package com.amirnlz.stylora.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.amirnlz.stylora.pages.dashboard.ui.DashboardScreen
import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.feedback.ui.FeedbackScreen
import com.amirnlz.stylora.pages.history.ui.HistoryScreen
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
                    val feedbackJson = Json.encodeToString(feedbackResponse)
                    navController.navigate(Routes.Feedback(feedbackJson))
                },
                onNavigateToHistory = {
                    navController.navigate(Routes.History)
                }
            )
        }
        composable<Routes.History> {
            HistoryScreen(
                onFeedbackClick = {
                    val feedbackJson = Json.encodeToString(it)
                    navController.navigate(Routes.Feedback(feedbackJson))
                },
                onBackClick = { navController.popBackStack() }
            )

        }
        composable<Routes.Feedback> { backStackEntry ->
            val feedbackJson = backStackEntry.toRoute<Routes.Feedback>().feedbackJson
            val feedbackResponse = Json.decodeFromString<FeedbackResponse>(feedbackJson)
            FeedbackScreen(feedback = feedbackResponse) {
                navController.popBackStack()
            }
        }
    }
}