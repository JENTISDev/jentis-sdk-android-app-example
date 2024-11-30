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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jentis.jentisappexample.fragments.localdata.SharedPreferencesManager
import com.jentis.sdk.jentissdk.JentisTrackService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationScreen(navController: NavController) {
    val context = LocalContext.current
    val prefsManager = remember { SharedPreferencesManager(context) }
    val protocol = remember { mutableStateOf(prefsManager.getString("protocol", "https://")) }
    val trackDomain = remember {
        mutableStateOf(
            prefsManager.getString(
                "trackDomain",
                "nd7cud.mobiweb.jtm-demo.com"
            )
        )
    }
    val container =
        remember { mutableStateOf(prefsManager.getString("container", "mobiweb-demoshop")) }
    val version = remember { mutableStateOf(prefsManager.getString("version", "1")) }
    val debugCode = remember {
        mutableStateOf(
            prefsManager.getString(
                "debugCode",
                "44c2acd3-43d4-4234-983b-48e91"
            )
        )
    }
    val sessionTimeout =
        remember { mutableStateOf(prefsManager.getString("sessionTimeout", "180")) }
    val environment = remember { mutableStateOf(prefsManager.getString("environment", "live")) }
    val isDebuggingEnabled = remember {
        mutableStateOf(prefsManager.getString("enabledDebugging", "true") == "true")
    }

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
                ProtocolSelector(protocol.value) { protocol.value = it }
                InputField("Track Domain", trackDomain.value) { trackDomain.value = it }
                InputField("Container", container.value) { container.value = it }

                DebuggingSwitch(isDebuggingEnabled.value) { isDebuggingEnabled.value = it }

                InputFieldWithEnabled(
                    "Version",
                    version.value,
                    onValueChange = { version.value = it },
                    isEnabled = isDebuggingEnabled.value
                )
                InputFieldWithEnabled(
                    "Debug Code",
                    debugCode.value,
                    onValueChange = { debugCode.value = it },
                    isEnabled = isDebuggingEnabled.value
                )
                InputField(
                    "Session Timeout (seconds)",
                    sessionTimeout.value
                ) { sessionTimeout.value = it }
                EnvironmentSelector(environment.value) { environment.value = it }
                Spacer(modifier = Modifier.height(20.dp))
                SaveButton(
                    context = context,
                    prefsManager = prefsManager,
                    protocol = protocol.value,
                    trackDomain = trackDomain.value,
                    container = container.value,
                    version = version.value,
                    debugCode = debugCode.value,
                    sessionTimeout = sessionTimeout.value,
                    environment = environment.value,
                    isDebuggingEnabled = isDebuggingEnabled.value
                )
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
fun DebuggingSwitch(isEnabled: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Enable Debugging", style = MaterialTheme.typography.bodyLarge)
        androidx.compose.material3.Switch(
            checked = isEnabled,
            onCheckedChange = onToggle
        )
    }
}

@Composable
fun InputFieldWithEnabled(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabled
    )
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
fun SaveButton(
    context: android.content.Context,
    prefsManager: SharedPreferencesManager,
    trackDomain: String,
    container: String,
    environment: String,
    version: String,
    debugCode: String,
    sessionTimeout: String,
    protocol: String,
    isDebuggingEnabled: Boolean
) {
    Button(onClick = {
        prefsManager.saveString("protocol", protocol)
        prefsManager.saveString("trackDomain", trackDomain)
        prefsManager.saveString("container", container)
        prefsManager.saveString("sessionTimeout", sessionTimeout)
        prefsManager.saveString("environment", environment)
        prefsManager.saveString("enabledDebugging", isDebuggingEnabled.toString())

        val versionToSave = if (isDebuggingEnabled) version else ""
        val debugCodeToSave = if (isDebuggingEnabled) debugCode else ""

        prefsManager.saveString("version", versionToSave)
        prefsManager.saveString("debugCode", debugCodeToSave)

        JentisTrackService.getInstance().restartConfig(
            trackDomain = trackDomain,
            container = container,
            environment = environment,
            version = versionToSave,
            debugCode = debugCodeToSave,
            sessionTimeoutParam = sessionTimeout.toIntOrNull(),
            authToken = null,
            protocol = protocol
        )

        Toast.makeText(context, "Configuration saved successfully", Toast.LENGTH_SHORT).show()
    }) {
        Text("Save configuration")
    }
}
