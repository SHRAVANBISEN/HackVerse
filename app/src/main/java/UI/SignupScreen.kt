package UI
import Extras.Result
import MapApi.MapPickerActivity
import ViewModels.AuthViewModel
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.welfare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var homeLatitude by remember { mutableStateOf("") }
    var homeLongitude by remember { mutableStateOf("") }

    // Dropdown for Roles
    var expandedRole by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("") }
    val roleOptions = listOf("Citizen", "Municipal Corporation", "NGO")

    // Observing authentication result
    val authResult by authViewModel.authResult.observeAsState()

    // Map Picker Launcher
    val context = LocalContext.current
    val mapPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val latitude = data?.getDoubleExtra("latitude", 0.0) ?: 0.0
            val longitude = data?.getDoubleExtra("longitude", 0.0) ?: 0.0
            homeLatitude = latitude.toString()
            homeLongitude = longitude.toString()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF180b42)),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.luser), // Placeholder for signup logo
                    contentDescription = "Sign-Up Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 16.dp)
                )
            }

            item {
                Text(
                    "Sign Up",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Input Fields for Name, Email, Password, Address, PIN Code, City, and District
            item { createTextField("Full Name", fullName) { fullName = it } }
            item { createTextField("Email", email) { email = it } }
            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { createTextField("Address", address) { address = it } }
            item { createTextField("PIN Code", pinCode) { pinCode = it } }
            item { createTextField("City", city) {} }
            item { createTextField("District", district) {} }

            // Role Selection
            item {
                DropdownField(
                    label = "Select Role",
                    options = roleOptions,
                    expanded = expandedRole,
                    selectedOption = selectedRole,
                    onOptionSelected = { selectedRole = it; expandedRole = false },
                    onExpandedChange = { expandedRole = !expandedRole },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Home Location Picker
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(60.dp)

                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Latitude: $homeLatitude, Longitude: $homeLongitude", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val intent = Intent(context, MapPickerActivity::class.java)
                        mapPickerLauncher.launch(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pick Home Location")
                }
            }

            // Signup Button
            item {
                Button(
                    onClick = {
                        authViewModel.signUp(
                            email = email,
                            password = password,
                            fullName = fullName,
                            address = address,
                            pinCode = pinCode,
                            city = city,
                            district = district,
                            role = selectedRole,
                            homeLatitude = homeLatitude.toDoubleOrNull(),
                            homeLongitude = homeLongitude.toDoubleOrNull()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("SIGN UP", color = Color.White, fontSize = 16.sp)
                }
            }

            // Login Navigation
            item {
                Text(
                    "Already have an account? Sign in.",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            authViewModel.clearAllAuthStates()
                            onNavigateToLogin()
                        }
                )
            }
        }
    }
}

@Composable
fun createTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}


// Mock function for autofilling city and district
fun getCityAndDistrictFromPin(pinCode: String): Pair<String, String> {
    return Pair("Mock City", "Mock District") // Replace with API or database lookup
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    expanded: Boolean,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onExpandedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange() }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        onExpandedChange()
                    }
                )
            }
        }
    }
}
