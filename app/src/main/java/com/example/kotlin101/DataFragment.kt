package com.example.kotlin101

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope

// Fragment of the data page
class DataFragment : Fragment() {

    private lateinit var composeView: ComposeView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: MainActivityViewModel by activityViewModels()

        // Collection of countries' data variables from the flow
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.countries.collect {
                if (viewModel.countries.value.isNotEmpty()) {
                    composeView.setContent {
                        DataScreen(
                            country = viewModel.countries.value[viewModel.countryIndex.value]
                        )
                    }
                }
            }
        }
    }

}