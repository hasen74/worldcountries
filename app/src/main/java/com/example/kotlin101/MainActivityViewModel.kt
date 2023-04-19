package com.example.kotlin101

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.kotlin101.data.Country
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import java.util.*

class MainActivityViewModel: ViewModel() {
    private val gson = Gson()

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    private val _countryIndex = MutableStateFlow(0)
    val countries: StateFlow<List<Country>> = _countries
    val countryIndex: StateFlow<Int> = _countryIndex


    private val _selectedField = mutableStateOf("Name")
    private val _selectedTimezone = mutableStateOf("UTC")
    private val _searchQuery = mutableStateOf("")
    private val _timezones = mutableStateOf(
        listOf("UTC-12:00", "UTC-11:00", "UTC-10:00", "UTC-09:30", "UTC-09:00", "UTC-08:00",
            "UTC-07:00", "UTC-06:00", "UTC-05:00", "UTC-04:00", "UTC-03:30", "UTC-03:00",
            "UTC-02:00", "UTC-01:00", "UTC+00:00", "UTC", "UTC+01:00", "UTC+02:00", "UTC+03:00",
            "UTC+03:30", "UTC+04:00", "UTC+04:30", "UTC+05:00", "UTC+05:30", "UTC+05:45",
            "UTC+06:00", "UTC+06:30", "UTC+07:00", "UTC+08:00", "UTC+09:00", "UTC+09:30",
            "UTC+10:00", "UTC+10:30", "UTC+11:00", "UTC+11:30", "UTC+12:00", "UTC+12:45",
            "UTC+13:00", "UTC+14:00")
    )
    val selectedField: State<String> = _selectedField
    val selectedTimezone: State<String> = _selectedTimezone
    val searchQuery: State<String> = _searchQuery
    val timezones: State<List<String>> = _timezones

    // Function to make an API call and get all countries data
    private fun getCountriesData() {
        Fuel
            .get("https://restcountries.com/v3.1/all")
            .response { _, _, result ->
                // declare data and error from result
                val (data, error) = result
                if (error == null) {
                    // whenever data is null, returns an empty byteArray so it doesn't
                    // crash when parsed to string
                    val json = String(data ?: byteArrayOf())
                    _countries.update {
                        gson.fromJson(json, Array<Country>::class.java)
                            .sortedWith(compareBy { it.name.common })
                    }
                } else {
                    println("no data")
                    println(error)
                }
            }
    }

    // Function to update which country is displayed in data screen
    fun getCountryIndex(index: Int) {
        _countryIndex.updateAndGet {  index }
    }

    // Function to update which field is used in the search bar
    fun fieldChange(newField: String) {
        _selectedField.value = newField
    }

    // Function to update the search object
    fun searchChange(newSearch: String) {
        _searchQuery.value = newSearch
    }

    // Function to update which timezone is displayed in search
    fun timezoneChange(newTimezone: String) {
        _selectedTimezone.value = newTimezone
    }

    init {
        getCountriesData()
    }
}

