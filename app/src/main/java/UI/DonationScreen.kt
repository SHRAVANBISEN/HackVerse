package UI

import Data.Donation
import MapApi.MapPickerActivity
import ViewModels.DonationState
import ViewModels.DonationViewModel
import ViewModels.DonationViewModelFactory
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationScreen(
    email: String,
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: DonationViewModel = viewModel(
        factory = DonationViewModelFactory(context)
    )
    val donationState by viewModel.donationState.collectAsState()

    var items by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<GeoPoint?>(null) }
    var pickupSchedule by remember { mutableStateOf("") }

    val locationPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val latitude = data?.getDoubleExtra("latitude", 0.0) ?: 0.0
            val longitude = data?.getDoubleExtra("longitude", 0.0) ?: 0.0
            location = GeoPoint(latitude, longitude)
        }
    }

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Add Donation Items",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )

            TextField(
                value = items,
                onValueChange = { items = it },
                label = { Text("Items (comma-separated)") },
                colors = TextFieldDefaults.textFieldColors(
                    Color.White
                )
            )

            TextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                colors = TextFieldDefaults.textFieldColors(
                     Color.White
                )
            )

            Button(onClick = {
                val intent = Intent(context, MapPickerActivity::class.java)
                locationPickerLauncher.launch(intent)
            }) {
                Text("Set Location on Map")
            }

            location?.let {
                Text("Selected Location: Lat ${it.latitude}, Lon ${it.longitude}")
            }

            TextField(
                value = pickupSchedule,
                onValueChange = { pickupSchedule = it },
                label = { Text("Pickup Schedule") },
                colors = TextFieldDefaults.textFieldColors(
                     Color.White
                )
            )

            Button(onClick = {
                if (items.isNotBlank() && address.isNotBlank() && location != null && pickupSchedule.isNotBlank()) {
                    val donation = Donation(
                        userEmail = email,
                        items = items.split(",").map { it.trim() },
                        address = address,
                        location = location,
                        pickupSchedule = pickupSchedule
                    )
                    viewModel.saveDonation(email, donation)
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Submit Donation")
            }

            when (donationState) {
                is DonationState.Loading -> CircularProgressIndicator()
                is DonationState.Success -> {
                    Toast.makeText(context, "Donation Submitted Successfully", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }
                is DonationState.Error -> Toast.makeText(context, "Failed to Submit Donation", Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
}
