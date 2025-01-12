package MapApi

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.welfare.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


class MapPickerActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            MapPickerScreen(
                onLocationSelected = { location ->
                    val resultIntent = Intent().apply {
                        putExtra("latitude", location.latitude)
                        putExtra("longitude", location.longitude)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                },
                onCurrentLocationRequested = {
                    requestCurrentLocation()
                }
            )
        }
    }

    private fun requestCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    setContent {
                        MapPickerScreen(
                            onLocationSelected = { location ->
                                val resultIntent = Intent().apply {
                                    putExtra("latitude", location.latitude)
                                    putExtra("longitude", location.longitude)
                                }
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            },
                            currentLocation = currentLatLng,
                            onCurrentLocationRequested = {
                                requestCurrentLocation()
                            }
                        )
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPickerScreen(
    onLocationSelected: (LatLng) -> Unit,
    initialLocation: LatLng = LatLng(28.6129, 77.2295),
    currentLocation: LatLng? = null,
    onCurrentLocationRequested: () -> Unit
) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 15f)
    }

    // Function to resize the PNG marker
    fun getResizedBitmap(resId: Int, width: Int, height: Int): Bitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, resId)
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    // Update the camera position when currentLocation is set
    currentLocation?.let {
        LaunchedEffect(it) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(it, 15f)
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pick a Location") },
                actions = {
                    Row {
                        TextButton(onClick = onCurrentLocationRequested) {
                            Text("Current Location")
                        }
                        TextButton(
                            onClick = {
                                selectedLocation?.let { onLocationSelected(it) }
                            },
                            enabled = selectedLocation != null
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            )
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        selectedLocation = latLng
                    }
                ) {
                    // Marker for selected location
                    selectedLocation?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Selected Location"
                        )
                    }

                    // Marker for current location with a resized PNG icon
                    currentLocation?.let {
                        val resizedBitmap = getResizedBitmap(R.drawable.blocationmarker, 100, 100)  // Adjust width and height as needed
                        Marker(
                            state = MarkerState(position = it),
                            title = "Your Location",
                            icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap)
                        )
                    }
                }
            }
        }
    )
}

