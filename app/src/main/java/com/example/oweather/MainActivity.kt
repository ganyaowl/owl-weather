package com.example.oweather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.result.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.oweather.ui.MainViewModel
import com.example.oweather.ui.theme.OWeatherTheme
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OWeatherTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen { navController.navigate("main") } }
                    composable("main") { MainScreen(navController = navController) }
                    composable("cities") { CitiesScreen(navController = navController) }
                    composable("map") { MapScreen() }
                    composable("cityDetail/{lat}/{lon}") { backStackEntry ->
                        CityDetailScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onComplete: () -> Unit) {
    LaunchedEffect(true) {
        delay(800)
        onComplete()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("OWeather", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
import androidx.navigation.NavController

fun MainScreen(viewModel: MainViewModel = hiltViewModel(), navController: NavController) {
    val forecast by viewModel.forecast.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val fused = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    var permissionGranted by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> permissionGranted = granted }

    LaunchedEffect(Unit) {
        permissionGranted = ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!permissionGranted) launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionGranted) {
            try {
                fused.lastLocation.addOnSuccessListener { loc ->
                    if (loc != null) viewModel.loadForecast(loc.latitude, loc.longitude)
                }
            } catch (_: Exception) {}
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("OWeather") }) }) { inner ->
        Column(modifier = Modifier.padding(inner).fillMaxSize().padding(16.dp)) {
            if (loading) CircularProgressIndicator()
            Spacer(Modifier.height(12.dp))
            Text(text = "Current:")
            Text(text = forecast?.current_weather?.temperature?.toString() ?: "--")
            Spacer(Modifier.height(12.dp))
            Text(text = "7-day forecast:")
            forecast?.daily?.time?.forEachIndexed { idx, day ->
                val max = forecast.daily?.temperature_2m_max?.getOrNull(idx)
                val min = forecast.daily?.temperature_2m_min?.getOrNull(idx)
                Text(text = "$day — ${min ?: "--"} / ${max ?: "--"}")
            }
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    // try to refresh current location
                    val fused = LocationServices.getFusedLocationProviderClient(LocalContext.current)
                    fused.lastLocation.addOnSuccessListener { loc -> if (loc != null) viewModel.loadForecast(loc.latitude, loc.longitude) }
                }) { Text("Refresh") }
                Button(onClick = { navController.navigate("cities") }) { Text("Cities") }
                Button(onClick = { navController.navigate("map") }) { Text("Map") }
            }
        }
    }
}

@Composable
import androidx.navigation.NavController

fun CitiesScreen(navController: NavController) {
    val viewModel: com.example.oweather.ui.CitiesViewModel = hiltViewModel()
    val cities by viewModel.cities.collectAsState()
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<com.example.oweather.network.model.GeoResult>>(emptyList()) }
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Search city") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row {
            Button(onClick = {
                scope.launch {
                    results = viewModel.search(query)
                }
            }) { Text("Search") }
            Spacer(Modifier.width(8.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text("Results:")
        Column(modifier = Modifier.weight(1f)) {
            results.forEach { r ->
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(r.name ?: "-")
                        Text(r.country ?: "")
                    }
                    Button(onClick = {
                        scope.launch { viewModel.addCity(r.name ?: "", r.latitude ?: 0.0, r.longitude ?: 0.0) }
                    }) { Text("Add") }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Saved cities:")
        Column(modifier = Modifier.fillMaxWidth()) {
            cities.forEach { c ->
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(c.name)
                    Row {
                        Button(onClick = { navController.navigate("cityDetail/${c.latitude}/${c.longitude}") }) { Text("Open") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { viewModel.deleteCity(c) }) { Text("Delete") }
                    }
                }
            }
        }
    }
}

@Composable
fun CityDetailScreen(viewModel: com.example.oweather.ui.CityDetailViewModel = hiltViewModel()) {
    val forecast by viewModel.forecast.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("City Detail", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("Current: ${forecast?.current_weather?.temperature ?: "--"}")
        Spacer(Modifier.height(8.dp))
        Text("7-day:")
        forecast?.daily?.time?.forEachIndexed { idx, day ->
            val max = forecast.daily?.temperature_2m_max?.getOrNull(idx)
            val min = forecast.daily?.temperature_2m_min?.getOrNull(idx)
            Text(text = "$day — ${min ?: "--"} / ${max ?: "--"}")
        }
    }
}

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val hasKey = try {
        val ai = context.packageManager.getApplicationInfo(context.packageName, android.content.pm.PackageManager.GET_META_DATA)
        val key = ai.metaData?.getString("com.google.android.geo.API_KEY")
        !key.isNullOrBlank()
    } catch (_: Exception) { false }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (hasKey) {
            Text("Map enabled — (Maps Compose view can be implemented)")
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Добавьте ключ Google Maps в AndroidManifest.xml")
                Spacer(Modifier.height(8.dp))
                Button(onClick = { /* open README or show instructions */ }) { Text("Открыть инструкцию") }
            }
        }
    }
}
