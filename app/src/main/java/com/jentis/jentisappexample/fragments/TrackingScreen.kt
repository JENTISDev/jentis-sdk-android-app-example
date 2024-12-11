package com.jentis.jentisappexample.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jentis.sdk.jentissdk.JentisTrackService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(navController: NavController) {
    val customInitiator = remember { mutableStateOf("") }
    val includeEnrichmentData = remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tracking Actions") },
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
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                // Include Enrichment Data
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Include Enrichment Data",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = includeEnrichmentData.value,
                        onCheckedChange = {
                            includeEnrichmentData.value = it
                            processEnrichment(it)
                        }
                    )
                }

                // Custom Initiator Field
                TextField(
                    value = customInitiator.value,
                    onValueChange = { customInitiator.value = it },
                    label = { Text("Custom Initiator (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Buttons Section
                TrackingButton(
                    label = "PageView",
                    backgroundColor = Color.Blue,
                    onClick = { addPageView(customInitiator.value) }
                )
                TrackingButton(
                    label = "ProductView",
                    backgroundColor = Color(0xFF9C27B0), // Purple
                    onClick = { addProductView(customInitiator.value) }
                )
                TrackingButton(
                    label = "Add-To-Cart",
                    backgroundColor = Color.Green,
                    onClick = { addToCart(customInitiator.value) }
                )
                TrackingButton(
                    label = "Order",
                    backgroundColor = Color(0xFFFFA500), // Orange
                    onClick = { addOrders(customInitiator.value) }
                )
                TrackingButton(
                    label = "Add Enrichment",
                    backgroundColor = Color(0xFFFF5722), // Deep Orange
                    onClick = { addEnrichment() }
                )
                TrackingButton(
                    label = "Custom Enrichment",
                    backgroundColor = Color.Gray,
                    onClick = { addCustomEnrichment() }
                )
            }
        }
    )
}

@Composable
fun TrackingButton(label: String, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                color = Color.White
            )
        }
    }
}

fun addPageView(customInitiator: String) {
    val mockPageView = listOf(
        mapOf(
            "track" to "pageview",
            "url" to "https://www.demoapp.com",
            "title" to "Demo-APP Pagetitle",
            "document_title" to "Demo-APP Document Title",
            "virtualPagePath" to "Track Pageview",
            "window_location_href" to "https://mipion.jtm-demo.com/NEWandroidtest"
        )
    )

    JentisTrackService.getInstance().push(mockPageView)

    JentisTrackService.getInstance().submit(customInitiator)
}

fun addProductView(customInitiator: String) {
    val mockValuesProducts = listOf(
        mapOf(
            "track" to "pageview",
            "pagetitle" to "Demo-APP Productview"
        ),
        mapOf(
            "track" to "product",
            "type" to "productview",
            "id" to "123",
            "name" to "Testproduct",
            "brutto" to 199.99
        ),
        mapOf(
            "track" to "productview"
        )
    )

    JentisTrackService.getInstance().push(mockValuesProducts)

    JentisTrackService.getInstance().submit(customInitiator)
}

fun addToCart(customInitiator: String) {
    val mockAddToCart = listOf(
        mapOf(
            "track" to "product",
            "type" to "addtocart",
            "id" to "123",
            "name" to "Testproduct",
            "brutto" to 199.99
        )
    )

    JentisTrackService.getInstance().push(mockAddToCart)

    JentisTrackService.getInstance().push(listOf(mapOf("track" to "addtocart")))

    JentisTrackService.getInstance().submit(customInitiator)
}

fun addOrders(customInitiator: String) {
    val mockOrders = listOf(
        mapOf(
            "track" to "pageview",
            "pagetitle" to "Demo-APP Order Confirmed"
        ),
        mapOf(
            "track" to "product",
            "type" to "order",
            "id" to "123",
            "name" to "Testproduct",
            "brutto" to 199.99
        ),
        mapOf(
            "track" to "product",
            "type" to "order",
            "id" to "456",
            "name" to "Testproduct 2",
            "brutto" to 299.99
        ),
        mapOf(
            "track" to "order",
            "orderid" to "12345666",
            "brutto" to 499.98,
            "paytype" to "creditcart"
        )
    )

    JentisTrackService.getInstance().push(mockOrders)

    JentisTrackService.getInstance().submit(customInitiator)
}

fun addEnrichment() {
    val mockEnrichment = listOf(
        mapOf(
            "track" to "pageview",
            "pagetitle" to "Demo-APP Order Confirmed",
            "account" to "JENTIS TEST ACCOUNT"
        ),
        mapOf(
            "track" to "product",
            "type" to "order",
            "id" to "123",
            "name" to "Testproduct",
            "brutto" to 199.99
        ),
        mapOf(
            "track" to "product",
            "type" to "currentcart",
            "id" to "777",
            "color" to "green"
        ),
        mapOf(
            "track" to "product",
            "type" to "order",
            "id" to "456",
            "name" to "Testproduct 2",
            "brutto" to 299.99
        ),
        mapOf(
            "track" to "order",
            "orderid" to "12345666",
            "brutto" to 499.98,
            "paytype" to "creditcart"
        )
    )

    val enrichmentMap = mapOf(
        "plugin" to mapOf(
            "pluginId" to "enrichment_xxxlprodfeed"
        ),
        "enrichment" to mapOf(
            "variablesEnrichment" to listOf("enrichment_product_variant")
        ),
        "args" to mapOf(
            "accountId" to "JENTIS TEST ACCOUNT",
            "page_title" to "Demo-APP Order Confirmed",
            "productId" to listOf("111", "222", "333", "444")
        )
    )

    JentisTrackService.getInstance()
        .addEnrichment(mockEnrichment, enrichmentMap)
}

fun addCustomEnrichment() {
    val enrichmentMap = mapOf(
        "plugin" to mapOf(
            "pluginId" to "enrichment_xxxlprodfeed_custom"
        ),
        "enrichment" to mapOf(
            "variablesEnrichment" to listOf("enrichment_product_variant")
        ),
        "args" to mapOf(
            "accountId" to "JENTIS TEST ACCOUNT",
            "page_title" to "Demo-APP Order Confirmed",
            "productId" to listOf("111", "222", "333", "444")
        )
    )

    JentisTrackService.getInstance()
        .addCustomEnrichment(enrichmentMap)
}

fun processEnrichment(checked: Boolean) {
    if (checked.not())
        JentisTrackService.getInstance().cleanEnrichments()
}
