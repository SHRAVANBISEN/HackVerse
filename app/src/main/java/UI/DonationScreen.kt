package eu.tutorials.mywishlistapp

import DonationViewModel
import Flow.Screen
import MapApi.MapPickerActivity
import ViewModels.AuthViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DonationScreen(
    navController: NavController,
    viewModel: DonationViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Black
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }

    val donationTitleState by viewModel.donationTitleState.collectAsState()
    val donationDescriptionState by viewModel.donationDescriptionState.collectAsState()
    val items = remember { mutableStateListOf<String>() }
    var newItemText by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var selectedCoordinatesText by remember { mutableStateOf("") }
    var blockchainMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val activity = LocalContext.current as Activity

    // Launcher for the MapPickerActivity
    val locationPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val latitude = result.data?.getDoubleExtra("latitude", 0.0) ?: 0.0
            val longitude = result.data?.getDoubleExtra("longitude", 0.0) ?: 0.0
            selectedLocation = GeoPoint(latitude, longitude)
            selectedCoordinatesText = "Lat: $latitude, Lng: $longitude"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            backgroundColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black
                    ),
                    title = { Text(text = "Add Donation", color = Color.Gray) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                OutlinedTextField(
                    value = donationTitleState,
                    onValueChange = { viewModel.onDonationTitleChanged(it) },
                    label = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = donationDescriptionState,
                    onValueChange = { viewModel.onDonationDescriptionChanged(it) },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItemText,
                        onValueChange = { newItemText = it },
                        label = { Text("Add Item") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    Button(
                        onClick = {
                            if (newItemText.isNotBlank()) {
                                items.add(newItemText)
                                newItemText = ""
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "Add")
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    items.forEach { item ->
                        Text(
                            text = "• $item",
                            modifier = Modifier.padding(start = 16.dp),
                            fontSize = 14.sp
                        )
                    }
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = selectedCoordinatesText,
                    onValueChange = { selectedCoordinatesText = it },
                    label = { Text("Selected Coordinates") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    readOnly = true
                )

                Button(
                    onClick = {
                        val intent = Intent(activity, MapPickerActivity::class.java)
                        locationPickerLauncher.launch(intent)
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Pick Location")
                }

                // Button for Adding to Firestore
                Button(
                    onClick = {
                        if (donationTitleState.isNotBlank() && donationDescriptionState.isNotBlank()) {
                            val userEmail = getUserIdFromAuth()
                            val userId = getUserIdFromAuthh()

                            if (selectedLocation == null) {
                                Toast.makeText(
                                    navController.context,
                                    "Please select a location",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            viewModel.addDonation(
                                userEmail = userEmail,
                                userId = userId,
                                useridd = userId,
                                items = items,
                                address = address,
                                location = selectedLocation!!,
                                pickupSchedule = "2025-01-15 10:00 AM"
                            )

                            scope.launch {
                                navController.navigateUp()
                                Toast.makeText(
                                    navController.context,
                                    "Donation added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Please fill all required fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Add Donation")
                }

                // Button for Adding to Blockchain
                Button(
                    onClick = {
                        if (donationTitleState.isNotBlank() && donationDescriptionState.isNotBlank()) {
                            if (selectedLocation == null) {
                                blockchainMessage = "Please select a location before adding to the blockchain."
                                return@Button
                            }

                            scope.launch {
                                val userEmail = getUserIdFromAuth()
                                val userId = getUserIdFromAuthh()
                                val createdAt = System.currentTimeMillis()
                                val addressDetails = "$address, Coordinates: ${selectedLocation!!.latitude}, ${selectedLocation!!.longitude}"

                                try {
                                    val response = viewModel.addDonationToBlockchain(
                                        id = createdAt.toString(), // Using createdAt as a unique identifier for simplicity
                                        userEmail = userEmail,
                                        items = items,
                                        addressDetails = addressDetails,
                                        pickupSchedule = "2025-01-15 10:00 AM",
                                        userId = userId,
                                        createdAt = createdAt
                                    )
                                    blockchainMessage = response
                                } catch (e: Exception) {
                                    blockchainMessage = "Error: ${e.message}"
                                }
                            }
                        } else {
                            blockchainMessage = "Please fill all required fields."
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Add to Blockchain")
                }


                if (blockchainMessage.isNotBlank()) {
                    Text(
                        text = blockchainMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

fun getUserIdFromAuth(): String {
    val auth = FirebaseAuth.getInstance()
    return auth.currentUser?.email ?: "user123@gmail.com"
}

fun getUserIdFromAuthh(): String {
    val auth = FirebaseAuth.getInstance()
    return auth.currentUser?.uid ?: "user123"
}
