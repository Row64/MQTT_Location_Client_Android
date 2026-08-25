package com.example.location_client_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment(R.layout.layout_fragment_home) {


    // TESTING *********************
    // ShareViewModel
//    val model = ViewModelProvider(requireActivity()).get(ViewModelPrimary::class.java)


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
        val view = inflater.inflate(R.layout.layout_fragment_login, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // Compose UI elements go here
                MaterialTheme {

                    TestLayout()

                }
            }
        }
        return view
    }



}

@Composable
fun TestLayout(viewModel: ViewModelPrimary = viewModel()) {

    // FOR TESTING
    Button(
        onClick = {
            // FOR TESTING ONLY!
            println("Home button:")
            // FOR TESTING
            viewModel.print()

        } )
    {
        Text(text = "Test")
    }
}
