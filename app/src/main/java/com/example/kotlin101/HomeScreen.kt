package com.example.kotlin101

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// UI element for the Home Fragment
@Composable
fun HomeScreen(
    viewModel: MainActivityViewModel,
    onClick: () -> Unit
) {
    // Initializing the variables to use with the search bar
    var displayCountry by remember { mutableStateOf(true) }
    var regex by remember { mutableStateOf(("").toRegex()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Row layout for the search bar elements
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)) {
            DropdownMenu(viewModel,
                listOf("Name", "Continent", "Language", "Currency", "Timezone"),
                "Search Field",
                0)
            if (viewModel.selectedField.value === "Timezone") {
                DropdownMenu(viewModel,
                    viewModel.timezones.value,
                    "Timezone",
                    15)
            }
            else {
                TextInput(viewModel)
            }
        }
        // Column layout for the list of countries
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(0.dp, 70.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mapping of the country objects from the ViewModel
            items(viewModel.countries.value) { country ->
                displayCountry = true
                // Checks if the user is using the search bar
                if(viewModel.searchQuery.value.isNotEmpty()
                    || viewModel.selectedField.value === "Timezone") {
                    regex = viewModel.searchQuery.value.toRegex(setOf(RegexOption.IGNORE_CASE))
                    displayCountry = resultFilter(country, viewModel, regex)
                }
                // Display the country objects as buttons if they pass the search bar check
                if(displayCountry) {
                    Button(onClick = {
                        viewModel.getCountryIndex(viewModel.countries.value.indexOf(country))
                        onClick()
                    },
                        border = null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp, 0.dp)
                    ) {
                        Text(text = country.flag + country.name.common + " " + country.continents
                            .toString()
                            .replace("[","(")
                            .replace("]", ")")
                        )
                    }
                }
            }
        }
    }
}