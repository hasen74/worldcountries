package com.example.kotlin101

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kotlin101.data.Country

// UI element for the dropdown search item selection menu
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownMenu(viewModel: MainActivityViewModel, options: List<String>, label: String, firstPosition: Int) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[firstPosition]) }

    // Menu container design & options
    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxHeight()
            .width(150.dp)
            .padding(5.dp, 0.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption,
            onValueChange = { },
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                backgroundColor = Color.White
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            // On selection, updates the ViewModel with the selected item
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = selectionOption
                        if (label === "Search Field") {
                            viewModel.fieldChange(selectedOption)
                            viewModel.searchChange("")
                        }
                        else if (label === "Timezone") {
                            viewModel.timezoneChange(selectedOption)
                        }
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

// UI element for the search bar text field
@Composable
fun TextInput(viewModel: MainActivityViewModel) {

    // Textfield design and options
    // On entry, updates the ViewModel with search text
    // via the searchChange function
    TextField(
        value = viewModel.searchQuery.value,
        onValueChange = {
            viewModel.searchChange(it)
        },
        placeholder = { Text("Search") },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        },
        trailingIcon = {
            if (viewModel.searchQuery.value.isNotEmpty()) {
                IconButton(onClick = {
                    viewModel.searchChange("")
                }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp, 5.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        )
    )
}

// Allows the homescreen UI composable to determine if a country
// can be displayed based on the search bar data
fun resultFilter(country: Country, viewModel: MainActivityViewModel, regex: Regex): Boolean {
    if (viewModel.selectedField.value === "Name") {
        if (regex.containsMatchIn(country.name.common)
            || regex.containsMatchIn(country.name.official)) {
            return true
        }
        else if (country.name.nativeName !== null) {
            country.name.nativeName.map {
                if (regex.containsMatchIn(it.key)) {
                    return true
                }
                if (regex.containsMatchIn(it.value.common)) {
                    return true
                }
                if (regex.containsMatchIn(it.value.official)) {
                    return true
                }
            }
        }
    }
    else if (viewModel.selectedField.value === "Continent") {
        country.continents.forEach {
            if (regex.containsMatchIn(it)) {
                return true
            }
        }
    }
    else if (viewModel.selectedField.value === "Language") {
        if (country.languages != null) {
            country.languages.values.forEach {
                if (regex.containsMatchIn(it)) {
                    return true
                }
            }
        }
    }
    else if (viewModel.selectedField.value === "Currency") {
        if (country.currencies !== null) {
            country.currencies.map {
                if (regex.containsMatchIn(it.key)) {
                    return true
                }
                if (regex.containsMatchIn(it.value.name)) {
                    return true
                }
                if (it.value.symbol !== null) {
                    if (regex.containsMatchIn(it.value.symbol)) {
                        return true
                    }
                }
            }
        }
    }
    else if (viewModel.selectedField.value === "Timezone") {
        if (country.timezones.contains(viewModel.selectedTimezone.value)) {
            return true
        }
    }
    return false
}