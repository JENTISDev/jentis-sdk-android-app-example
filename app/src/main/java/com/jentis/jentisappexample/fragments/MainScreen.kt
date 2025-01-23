package com.jentis.jentisappexample.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jentis.jentisappexample.R
import com.jentis.sdk.jentissdk.JentisTrackService

enum class ConsentOptions(val label: String) {
    ALLOW("true"),
    DENY("false"),
    NCM("ncm")
}

@Composable
fun VendorConsentBox(
    vendorConsents: Map<String, String>,
    onVendorConsentChange: (String, String) -> Unit,
    onVendorsSet: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE1F5FE))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            vendorConsents.forEach { (vendor, consent) ->
                VendorConsentItem(
                    vendor = vendor,
                    consent = consent,
                    onConsentChange = { newConsent ->
                        onVendorConsentChange(vendor, newConsent)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onVendorsSet,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .border(
                            width = 2.dp,
                            color = Color(0xFF0068A3),
                            shape = RoundedCornerShape(40.dp)
                        ),
                    shape = RoundedCornerShape(40.dp)
                ) {
                    Text(
                        text = "Set Vendors",
                        color = Color(0xFF0068A3)
                    )
                }
            }
        }
    }
}

@Composable
fun VendorConsentItem(
    vendor: String,
    consent: String,
    onConsentChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedConsent by remember { mutableStateOf(consent) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = vendor, modifier = Modifier.weight(1f))

        Box {
            TextButton(onClick = { expanded = true }) {
                Text(text = selectedConsent, color = Color.Black)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ConsentOptions.entries.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedConsent = option.label
                            onConsentChange(option.label)
                            expanded = false
                        },
                        text = { Text(option.label) }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName
    val versionCode = packageInfo.longVersionCode.toString()

    var isBoxVisible by remember { mutableStateOf(false) }
    var vendorConsents by remember { mutableStateOf(getInitialVendorConsents()) }

    Scaffold(
        topBar = { AppTopBar(navController) },
        content = { padding ->
            MainContent(
                padding = padding,
                isBoxVisible = isBoxVisible,
                onConsentClicked = { isBoxVisible = !isBoxVisible },
                vendorConsents = vendorConsents,
                onVendorConsentChange = { vendor, consent ->
                    vendorConsents = vendorConsents.toMutableMap().apply {
                        this[vendor] = consent
                    }
                },
                onVendorsSet = {
                    JentisTrackService.getInstance().setConsents(vendorConsents)
                    isBoxVisible = false
                },
                navController = navController,
                appVersion = "Version $versionName($versionCode)"
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Jentis SDK Demo") },
        actions = {
            IconButton(onClick = { navController.navigate("configuration") }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Config")
            }
        }
    )
}

@Composable
fun MainContent(
    padding: PaddingValues,
    isBoxVisible: Boolean,
    onConsentClicked: () -> Unit,
    vendorConsents: Map<String, String>,
    onVendorConsentChange: (String, String) -> Unit,
    onVendorsSet: () -> Unit,
    navController: NavController,
    appVersion: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.jentis_logo),
            contentDescription = "App Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onConsentClicked() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFFFFF)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFF0068A3),
                    shape = RoundedCornerShape(40.dp)
                )
        ) {
            Text(
                text = "Consent Modal",
                color = Color(0xFF0068A3)
            )
        }


        if (isBoxVisible) {
            VendorConsentBox(
                vendorConsents = vendorConsents,
                onVendorConsentChange = onVendorConsentChange,
                onVendorsSet = onVendorsSet
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("tracking")
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0068A3)),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Tracking Examples",
                color = Color(0xFFFFFFFF)
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = appVersion,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
    }
}

fun getInitialVendorConsents(): Map<String, String> {
    return mapOf(
        "google_analytics_4_server-side" to "true",
        "facebook" to "false",
        "adwords" to "ncm"
    )
}
