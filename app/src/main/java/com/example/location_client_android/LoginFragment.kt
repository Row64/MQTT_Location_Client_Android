package com.example.location_client_android

import android.R.attr.onClick
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.getValue


class LoginFragment : Fragment( /* R.layout.layout_fragment_login */ ) {

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
        val view = inflater.inflate(R.layout.layout_fragment_login, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // Compose UI elements go here
                MaterialTheme {

                    ConnectScreen(viewModel)


                }
            }
        }
        return view
    }





}


@Composable
fun ConnectScreen(viewModel: ViewModelPrimary) {

    // Layout
    Column(
        modifier = Modifier
            .padding(48.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // UI components
        Title()
//        FieldHost()
//        FieldPort()
//        FieldUser()
//        FieldPass()
//        ButtonConnect( onClick = { println("Connect button clicked...") } )
//        LoginComponents()


        // Login components
        var stateHost = rememberTextFieldState()
        var statePort = rememberTextFieldState()
        var stateUser = rememberTextFieldState()
        var statePass = rememberTextFieldState()


        // Host field
        OutlinedTextField(
            state = stateHost,
            label = { Text("Host") },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false
            )
        )


        // Port field
        OutlinedTextField(
            state = statePort,
            label = { Text("Port") },
            inputTransformation = InputTransformation.maxLength(5),
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Number
            )
        )


        // Username field
        OutlinedTextField(
            state = stateUser,
            label = { Text("User") },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false
            )
        )


        // Password field
        OutlinedSecureTextField(
            state = statePass,
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false
            )
        )


        // Connect button
        Button(
            onClick = {

                // FOR TESTING ONLY!
                println("RAW --------------------" +
                        "\nHost: ${stateHost.text}" +
                        "\nPort: ${statePort.text}" +
                        "\nUser: ${stateUser.text}" +
                        "\nPass: ${statePass.text}")

//                // FOR TESTING
//                println("Before assignment:")
//                viewModel.print()
////            viewModel.mqHost = stateHost.text.toString()
//                viewModel.mqHost.value = stateHost.text.toString()
//                println("After assignment:")
//                viewModel.print()

                // Disable the button (TEST VERSION)
                viewModel.toggleConnectBtn(false)


                // Send login data to the view model
                viewModel.inputHost.value = stateHost.text.toString()
                viewModel.inputPort.value = statePort.text.toString()
                viewModel.inputUser.value = stateUser.text.toString()
                viewModel.inputPass.value = statePass.text.toString()

                // Attempt the connection to the MQTT broker
                viewModel.tryConnect()

            },
            enabled = viewModel.connectBtnEnabled
        )
        {
            Text(text = stringResource(R.string.connect_btn_connect))

        }


        // Connection status
        /**
         * This should indicate to the user if the connection attempt was successful or not,
         * and print any returned MQTT connection issues.
         *
         * Consider placing in a text box. The text box should be centered, but the text within
         * the box should be left-aligned
         */
        Text(
            text = "CONNECTION ATTEMPT STATUS PLACEHOLDER..."
        )


    }
}

@Composable
fun Title() {
    Text(
        text = stringResource(R.string.login_screen_title),
        fontSize = 28.sp,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

/**
 * Pass the ViewModel into the composable.
 * https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state
 *
 * The ViewModel is not declared anywhere else; just present here as this parameter
 */
//@Composable
//fun LoginComponents(viewModel: ViewModelPrimary = viewModel()) {
//
//    // Login container variables
//    // For testing - might need to input into a ViewModel (?)
//    var stateHost = rememberTextFieldState()
//    var statePort = rememberTextFieldState()
//    var stateUser = rememberTextFieldState()
//    var statePass = rememberTextFieldState()
//
//
//    // Host field
//    OutlinedTextField(
//        state = stateHost,
//        label = { Text("Host") },
//        lineLimits = TextFieldLineLimits.SingleLine,
//        keyboardOptions = KeyboardOptions(
//            capitalization = KeyboardCapitalization.None,
//            autoCorrectEnabled = false
//        )
//    )
//
//
//    // Port field
//    OutlinedTextField(
//        state = statePort,
//        label = { Text("Port") },
//        inputTransformation = InputTransformation.maxLength(5),
//        lineLimits = TextFieldLineLimits.SingleLine,
//        keyboardOptions = KeyboardOptions(
//            capitalization = KeyboardCapitalization.None,
//            autoCorrectEnabled = false,
//            keyboardType = KeyboardType.Number
//        )
//    )
//
//
//    // Username field
//    OutlinedTextField(
//        state = stateUser,
//        label = { Text("User") },
//        lineLimits = TextFieldLineLimits.SingleLine,
//        keyboardOptions = KeyboardOptions(
//            capitalization = KeyboardCapitalization.None,
//            autoCorrectEnabled = false
//        )
//    )
//
//
//    // Password field
//    OutlinedSecureTextField(
//        state = statePass,
//        label = { Text("Password") },
//        keyboardOptions = KeyboardOptions(
//            capitalization = KeyboardCapitalization.None,
//            autoCorrectEnabled = false
//        )
//    )
//
//
//    // Connect button
//    Button(
//        onClick = {
//            // FOR TESTING ONLY!
//            println("RAW --------------------" +
//                    "\nHost: ${stateHost.text}" +
//                    "\nPort: ${statePort.text}" +
//                    "\nUser: ${stateUser.text}" +
//                    "\nPass: ${statePass.text}")
//
//            // FOR TESTING
//            println("Before assignment:")
//            viewModel.print()
////            viewModel.mqHost = stateHost.text.toString()
//            viewModel.mqHost.value = stateHost.text.toString()
//            println("After assignment:")
//            viewModel.print()
//
//
//        } )
//    {
//        Text(text = stringResource(R.string.connect_btn_connect))
//    }
//
//
//    // Connection status
//    /**
//     * This should indicate to the user if the connection attempt was successful or not,
//     * and print any returned MQTT connection issues.
//     *
//     * Consider placing in a text box. The text box should be centered, but the text within
//     * the box should be left-aligned
//     */
//    Text(
//        text = "CONNECTION ATTEMPT STATUS PLACEHOLDER..."
//    )
//
//
//
//
//}











//@Composable
//fun ButtonConnect( onClick: () -> Unit) {
//    Button(
//        onClick = {
//            onClick()
//        } )
//    {
//        Text(text = stringResource(R.string.connect_btn_connect))
//    }
//}
//
//@Composable
//fun FieldHost() {
//
////    val hostState = rememberTextFieldState()
//
//    OutlinedTextField(
////        state = hostState,
//        state = rememberTextFieldState(),
//        label = { Text("Host") },
//        lineLimits = TextFieldLineLimits.SingleLine,
//        keyboardOptions = KeyboardOptions(
//            capitalization = KeyboardCapitalization.None,
//            autoCorrectEnabled = false
//        )
//    )
//}
//
//@Composable
//fun FieldPort() {
//    OutlinedTextField(
//        state = rememberTextFieldState(),
//        label = { Text("Port") },
//        inputTransformation = InputTransformation.maxLength(5),
//        lineLimits = TextFieldLineLimits.SingleLine,
//        keyboardOptions = KeyboardOptions(
//            capitalization = KeyboardCapitalization.None,
//            autoCorrectEnabled = false,
//            keyboardType = KeyboardType.Number
//        )
//    )
//}
//
//@Composable
//fun FieldUser() {
//    OutlinedTextField(
//        state = rememberTextFieldState(),
//        label = { Text("User") },
//        lineLimits = TextFieldLineLimits.SingleLine,
//        keyboardOptions = KeyboardOptions(
//            capitalization = KeyboardCapitalization.None,
//            autoCorrectEnabled = false
//        )
//    )
//}
//
//@Composable
//fun FieldPass() {
//    OutlinedSecureTextField(
//        state = rememberTextFieldState(),
//        label = { Text("Password") },
//        keyboardOptions = KeyboardOptions(
//            capitalization = KeyboardCapitalization.None,
//            autoCorrectEnabled = false
//        )
//    )
//}

