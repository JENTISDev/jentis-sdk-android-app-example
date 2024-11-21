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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jentis.sdk.jentissdk.JentisTrackService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationScreen(navController: NavController) {
    var trackDomain by remember { mutableStateOf("nd7cud.mobiweb.jtm-demo.com") }
    var container by remember { mutableStateOf("mobiweb-demoshop") }
    var version by remember { mutableStateOf("1") }
    var debugCode by remember { mutableStateOf("44c2acd3-43d4-4234-983b-48e91") }
    var sessionTimeout by remember { mutableStateOf("180") }
    var environment by remember { mutableStateOf("live") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuration") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = trackDomain,
                    onValueChange = { trackDomain = it },
                    label = { Text("Track Domain") },
                    modifier = Modifier.fillMaxWidth().padding(top = 48.dp)
                )

                OutlinedTextField(
                    value = container,
                    onValueChange = { container = it },
                    label = { Text("Container") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                OutlinedTextField(
                    value = version,
                    onValueChange = { version = it },
                    label = { Text("Version") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                OutlinedTextField(
                    value = debugCode,
                    onValueChange = { debugCode = it },
                    label = { Text("Debug Code") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                OutlinedTextField(
                    value = sessionTimeout,
                    onValueChange = { sessionTimeout = it },
                    label = { Text("Session Timeout (seconds)") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Environment")
                    Row {
                        RadioButton(
                            selected = environment == "live",
                            onClick = { environment = "live" }
                        )
                        Text("Live")
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(
                            selected = environment == "stage",
                            onClick = { environment = "stage" }
                        )
                        Text("Stage")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    JentisTrackService.getInstance().restartConfig(
                        trackDomain = trackDomain,
                        container = container,
                        environment = environment,
                        version = version,
                        debugCode = debugCode,
                        sessionTimeoutParam = sessionTimeout.toIntOrNull(),
                        authToken = null
                    )

                    Toast.makeText(
                        context,
                        "Configuration saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text("Save")
                }
            }
        }
    )
}
