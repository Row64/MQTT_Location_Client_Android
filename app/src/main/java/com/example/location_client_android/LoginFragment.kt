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


val host: String = ""


class LoginFragment : Fragment( /* R.layout.layout_fragment_login */ ) {

    val login = MqLogin()


    /**
     * Enables the use of Composables in a classic fragment.
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

                    ConnectScreen()




                }
            }
        }
        return view
    }





}


@Composable
fun ConnectScreen() {

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
//        Label()
        FieldHost()
        FieldPort()
        FieldUser()
        FieldPass()
        ButtonConnect( onClick = { println("Connect button clicked...") } )

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

@Composable
fun Label() {
    Text(
        text = stringResource(R.string.login_screen_label),
        fontSize = 15.sp,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
fun ButtonConnect( onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        } )
    {
        Text(text = stringResource(R.string.connect_btn_connect))
    }
}

@Composable
fun FieldHost() {

    val hostState = rememberTextFieldState()

    OutlinedTextField(
        state = hostState,
        label = { Text("Host") },
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false
        )
    )
}

@Composable
fun FieldPort() {
    OutlinedTextField(
        state = rememberTextFieldState(),
        label = { Text("Port") },
        inputTransformation = InputTransformation.maxLength(4),
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun FieldUser() {
    OutlinedTextField(
        state = rememberTextFieldState(),
        label = { Text("User") },
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false
        )
    )
}

@Composable
fun FieldPass() {
    OutlinedSecureTextField(
        state = rememberTextFieldState(),
        label = { Text("Password") },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false
        )
    )
}

