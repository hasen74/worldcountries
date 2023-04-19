package com.example.kotlin101

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kotlin101.data.Country
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// UI element for the data fragment
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DataScreen(
    country: Country
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Initializing the variables from the object data,
        // checking if they are not null to prevent app crash
        var language = "No language found."
        var currency = "No currency found."
        var timezones = "No timezones found."
        var wikiUrl = "https://en.wikipedia.org/wiki/" + country.name.common
            .replace(" ", "_")
        val handler = LocalUriHandler.current
        if(country.languages != null && country.languages.count() == 1) {
            language = "Language: " + country.languages.values.toString()
                .replace("[","")
                .replace("]", "")
        } else if(country.languages != null && country.languages.count() > 1) {
            language = "Languages: " + country.languages.values.toString()
                .replace("[","")
                .replace("]", "")
        }

        if(country.currencies != null && country.currencies.count() == 1) {
            currency = "Currency: " + country.currencies.values.map { it.name }.toString()
                .replace("[","")
                .replace("]", "") + country.currencies.keys.toString()
                .replace("["," (").replace("]", ")")
        } else if(country.currencies != null && country.currencies.count() > 1) {
        currency = "Currencies: " + country.currencies.values.map { it.name }.toString()
            .replace("[","")
            .replace("]", "") + country.currencies.keys.toString()
            .replace("["," (").replace("]", ")")
    }
        if(country.timezones != null && country.timezones.count() == 1) {
            timezones = "Timezone: " + country.timezones.toString()
                .replace("[","")
                .replace("]", "")
        } else  if(country.timezones != null && country.timezones.count() > 1) {
            timezones = "Timezones: " + country.timezones.toString()
                .replace("[","")
                .replace("]", "")
        }
        if(country.flags.png !== null) {
            GlideImage(
                model = country.flags.png,
                contentDescription = "Drapeau",
                modifier = Modifier
                    .padding(Dp(0F),Dp(0F), Dp(0F), Dp(20F))
                    .border(1.dp, Color.LightGray)
            )
        }
        Text(
            text = country.name.common,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = country.continents.toString()
            .replace("[","(")
            .replace("]", ")"))
        Text(text = language)
        Text(text = currency)
        Text(text = timezones)
        Button(onClick = {
            handler.openUri(wikiUrl)
        },
            border = null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White),
            modifier = Modifier.padding(Dp(0F),Dp(0F), Dp(0F), Dp(20F))
        ) {
            Text(text = "Wikipedia Page")
        }
        MapUI(country)
    }
}

// UI element for the map
@Composable
fun MapUI(country: Country) {
    var latitude = 0.0
    var longitude = 0.0

    // Getting the lat and long data from the object
    if(country.capitalInfo.latlng !== null) {
        latitude = country.capitalInfo.latlng[0]
        longitude = country.capitalInfo.latlng[1]
    } else if(country.name.common === "Antarctica") {
        latitude = -76.215426
        longitude = 22.387430
    }

    val countryLatLng = LatLng(latitude, longitude)
    val countryState = MarkerState(position = countryLatLng)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(countryLatLng, 5f)
    }
    Column {}
    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier
            .width(Dp(300F))
            .height(Dp(300F)),
        // Display the marker on the capital
    ) { Marker(
            state = countryState,
            draggable = true,
            title = "${country.name.common}"
        )
    }
}