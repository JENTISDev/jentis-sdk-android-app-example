package com.jentis.jentisappexample.fragments

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jentis.sdk.jentissdk.JentisTrackService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationScreen(navController: NavController) {
    // State variables
    var protocol by remember { mutableStateOf("https://") }
    var trackDomain by remember { mutableStateOf("nd7cud.mobiweb.jtm-demo.com") }
    var container by remember { mutableStateOf("mobiweb-demoshop") }
    var version by remember { mutableStateOf("1") }
    var debugCode by remember { mutableStateOf("44c2acd3-43d4-4234-983b-48e91") }
    var sessionTimeout by remember { mutableStateOf("180") }
    var environment by remember { mutableStateOf("live") }
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { ConfigurationTopBar(navController) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProtocolSelector(protocol) { protocol = it }
                InputField("Track Domain", trackDomain) { trackDomain = it }
                InputField("Container", container) { container = it }
                InputField("Version", version) { version = it }
                InputField("Debug Code", debugCode) { debugCode = it }
                InputField("Session Timeout (seconds)", sessionTimeout) { sessionTimeout = it }
                EnvironmentSelector(environment) { environment = it }
                Spacer(modifier = Modifier.height(20.dp))
                SaveButton(
                    context = context,
                    trackDomain = trackDomain,
                    container = container,
                    environment = environment,
                    version = version,
                    debugCode = debugCode,
                    sessionTimeout = sessionTimeout,
                    protocol = protocol
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Configuration") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
fun ProtocolSelector(selectedProtocol: String, onProtocolChange: (String) -> Unit) {
    Column {
        Text("Protocol", style = MaterialTheme.typography.bodyLarge)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(
                selected = selectedProtocol == "https://",
                onClick = { onProtocolChange("https://") }
            )
            Text("https://", modifier = Modifier.padding(end = 16.dp))
            RadioButton(
                selected = selectedProtocol == "http://",
                onClick = { onProtocolChange("http://") }
            )
            Text("http://")
        }
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun EnvironmentSelector(selectedEnvironment: String, onEnvironmentChange: (String) -> Unit) {
    Column {
        Text("Environment", style = MaterialTheme.typography.bodyLarge)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedEnvironment == "live",
                    onClick = { onEnvironmentChange("live") }
                )
                Text("Live", modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedEnvironment == "stage",
                    onClick = { onEnvironmentChange("stage") }
                )
                Text("Stage", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun SaveButton(
    context: android.content.Context,
    trackDomain: String,
    container: String,
    environment: String,
    version: String,
    debugCode: String,
    sessionTimeout: String,
    protocol: String
) {
    Button(onClick = {
        JentisTrackService.getInstance().restartConfig(
            trackDomain = trackDomain,
            container = container,
            environment = environment,
            version = version,
            debugCode = debugCode,
            sessionTimeoutParam = sessionTimeout.toIntOrNull(),
            authToken = null,
            protocol = protocol
        )
        Toast.makeText(context, "Configuration saved successfully", Toast.LENGTH_SHORT).show()
    }) {
        Text("Save")
    }
}
