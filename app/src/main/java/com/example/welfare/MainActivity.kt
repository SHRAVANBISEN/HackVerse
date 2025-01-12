package com.example.welfare

import DonationViewModel
import Flow.NavigationGraph
import UI.ReportGarbageScreen
import ViewModels.AuthViewModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.welfare.ui.theme.WelfareTheme
import com.google.firebase.firestore.GeoPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
  val DonationViewModel  :DonationViewModel = viewModel()
            WelfareTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
   NavigationGraph(navController =navController  ,authViewModel , donationViewModel = DonationViewModel)                }
            }
        }
    }    private val donationViewModel: DonationViewModel by viewModels()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val latitude = data?.getDoubleExtra("latitude", 0.0)
            val longitude = data?.getDoubleExtra("longitude", 0.0)

            if (latitude != null && longitude != null) {
                val geoPoint = GeoPoint(latitude, longitude)
                donationViewModel.updateSelectedLocation(geoPoint) // Update the ViewModel
            }
        }
    }
}

