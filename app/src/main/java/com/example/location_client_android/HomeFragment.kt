package com.example.location_client_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

class HomeFragment : Fragment(R.layout.layout_fragment_home) {

    // https://developer.android.com/guide/fragments/communicate#viewmodel

    // TESTING *********************
    // ShareViewModel
//    val model = ViewModelProvider(requireActivity()).get(ViewModelPrimary::class.java)
    private val viewModel: ViewModelPrimary by activityViewModels()


    /**
     * Enables the use of Composables in a legacy fragment.
     * This requires the XML layout to have a ComposeView block
     *
     * https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/compose-in-views#compose-in-fragments
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        // Observe data from the view model
//        viewModel.mqHost.observe(viewLifecycleOwner) { data ->
//            println(data) // Print the value of mqHost to the terminal - FOR TESTING
//        }

        val view = inflater.inflate(R.layout.layout_fragment_login, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // Compose UI elements go here
                MaterialTheme {

                    Column(
                        modifier = Modifier
                            .padding(48.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        MainTitle()
                        R64Image()
                        BodyText()
                    }






                }
            }
        }
        return view
    }



}


@Composable
fun MainTitle() {
    Text(
        text = "Row64 Client Signal Generator",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier
            .padding(bottom = 20.dp)
    )
}

@Composable
fun R64Image() {
    Image(
        painter = painterResource(id = R.drawable.r64_mqtt_client_image),
        contentDescription = null,
        modifier = Modifier
            .padding(bottom = 20.dp)
    )
}

@Composable
fun BodyText() {
    Text(
        text = "Supported MQTT version:\n5, 3.1.1\n\n" +
                "MQTT message topic: R64_LOCATION_UPDATE\n\n" +
                "Update interval rate:\n5 seconds",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .height(300.dp)
    )
}
