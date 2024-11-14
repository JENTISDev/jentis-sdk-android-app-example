package com.jentis.jentisappexample.fragments

import android.content.Context
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jentis.sdk.jentissdk.JentisTrackService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationScreen(navController: NavController) {
    val context = navController.context
    val sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    var trackDomain by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getString(
                "trackDomain",
                "nd7cud.mobiweb.jt-demo.com"
            ) ?: ""
        )
    }
    var container by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getString(
                "container",
                "mobiweb-demoshop"
            ) ?: ""
        )
    }
    var version by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getString("version", "1") ?: ""
        )
    }
    var debugCode by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getString(
                "debugCode",
                "44c2acd3-43d4-4234-983b-48e91..."
            ) ?: ""
        )
    }
    var environment by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getString(
                "environment",
                "live"
            ) ?: "live"
        )
    }
    var authToken by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getString("token", "1") ?: ""
        )
    }

    fun saveConfiguration() {
        with(sharedPreferences.edit()) {
            putString("trackDomain", trackDomain)
            putString("container", container)
            putString("version", version)
            putString("debugCode", debugCode)
            putString("environment", environment)
            putString("token", authToken)
            apply()
        }

        JentisTrackService.getInstance().restartConfig(
            trackDomain = trackDomain,
            container = container,
            environment = environment,
            version = version,
            debugCode = debugCode,
            authToken = authToken
        )
    }

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
                    value = authToken,
                    onValueChange = { authToken = it },
                    label = { Text("Auth Token") },
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
                    saveConfiguration()
                    navController.popBackStack()
                }) {
                    Text("Save")
                }
            }
        }
    )
}
