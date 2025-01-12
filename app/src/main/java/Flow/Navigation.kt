package Flow

import DonationViewModel
import UI.DefaultScreen
import UI.GarbageReportFormScreen
import UI.LoginScreen
import UI.ReportGarbageScreen
import UI.SelectImage
import UI.SignupScreen
import ViewModels.AuthViewModel
import ViewModels.ReportGarbageViewModel
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.tutorials.mywishlistapp.DonationScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    donationViewModel: DonationViewModel



) {
    val context = navController.context

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.DefaultScreen.route) {

            DefaultScreen(navController ,authViewModel)
        }

        composable(Screen.LoginScreen.route) {
            val context = LocalContext.current
            LoginScreen(
                authViewModel = authViewModel,
                context = context,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onSignInSuccess = {   navController.navigate(Screen.DefaultScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                } }) {

            }
        }
        composable(
            route = "${Screen.FormScreen.route}?imageUri={imageUri}",
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageUriString = backStackEntry.arguments?.getString("imageUri")
            val imageUri = imageUriString?.let { Uri.parse(it) } ?: Uri.EMPTY
            GarbageReportFormScreen(navController, capturedImageUri = imageUri)
        }

        composable(Screen.SignupScreen.route) {
            SignupScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route)
                {
                    popUpTo(Screen.SignupScreen.route) { inclusive = true }
                }}
            )
        }
        composable(Screen.CaptureImageScreen.route) {

            ReportGarbageScreen(navController )
        }
        composable(Screen.donation.route) {
            DonationScreen(navController = navController)
        }
        composable("${Screen.donation.route}/{wishId}") { backStackEntry ->
            val wishIdString = backStackEntry.arguments?.getString("wishId") ?: "0"
            val wishId = wishIdString.toLongOrNull() ?: 0L

            // Pass wishId if DonationScreen requires it in future
            DonationScreen(
                navController = navController,
                viewModel = donationViewModel
            )
        }

        composable(Screen.SelectImageScreen.route) {

            SelectImage(navController)
        }


    }
}