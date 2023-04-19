package com.example.kotlin101

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

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

                // If countries array is filled, passes parameters to the HomeScreen composable
                if (viewModel.countries.value.isNotEmpty()) {
                    composeView.setContent {
                        HomeScreen(
                            viewModel = viewModel,
                            onClick = {
                                findNavController()
                                    .navigate(R.id.action_homeFragment_to_dataFragment)
                            }
                        )
                    }
                }
            }
        }
    }

}