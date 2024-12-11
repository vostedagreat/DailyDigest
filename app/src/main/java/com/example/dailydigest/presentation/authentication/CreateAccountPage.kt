package com.example.dailydigest.presentation.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.dailydigest.Validate.RegistrationFormEvent
import com.example.dailydigest.modules.AppButton
import com.example.dailydigest.modules.AppTextField
import com.example.dailydigest.presentation.HomePage
import com.example.dailydigest.viewmodels.AuthenticationViewModel
import domain.models.ResultStatus
import org.koin.compose.koinInject

class CreateAccountPage : Screen {
    @Composable
    override fun Content() {
        CreateAccountScreen(modifier = Modifier)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateAccountScreen(modifier : Modifier = Modifier) {

        val navigator = LocalNavigator.currentOrThrow

        var passwordVisible by remember { mutableStateOf(false) }

        val authenticationViewModel = koinInject<AuthenticationViewModel>()
        val signupState by authenticationViewModel.signUpState.collectAsStateWithLifecycle()
        val pageState = authenticationViewModel.state

        LaunchedEffect (signupState.status) {
            if (signupState.status == ResultStatus.SUCCESS) {
                navigator.replaceAll(HomePage())
            }

        }
        Scaffold(

        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)

            ) {


                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
                item {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        IconButton(onClick = { navigator.pop() }) {

                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")

                        }
                        Text(
                            text = "Create a new account",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                }
                item {
                    AppTextField(
                        value = pageState.name,
                        placeholder = "Enter your Username",
                        error = pageState.nameError,
                        onValueChanged = { authenticationViewModel.onEvent(RegistrationFormEvent.NameChanged(it)) },
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier
                    )
                }
                item {
                    AppTextField(
                        value = pageState.email,
                        placeholder = "Email",
                        error = pageState.emailError,
                        onValueChanged = { authenticationViewModel.onEvent(RegistrationFormEvent.EmailChanged(it))},
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier
                    )
                }
                item {
                    AppTextField(
                        value = pageState.password,
                        onValueChanged = {authenticationViewModel.onEvent(RegistrationFormEvent.PasswordChanged(it)) },
                        error = pageState.passwordError,
                        placeholder = "New Password",
                        keyboardType = KeyboardType.Password,
                        passwordVisible = passwordVisible,
                        imeAction = ImeAction.Done,
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Lock else Icons.Default.Lock,
                                    contentDescription = "show password"
                                )
                            }
                        }
                    )

                }
                item {
                    AppTextField(
                        value = pageState.confirmPassword,
                        onValueChanged = { authenticationViewModel.onEvent(RegistrationFormEvent.ConfirmPassword(it))},
                        error = pageState.confirmPasswordError,
                        placeholder = "Confirm Password",
                        keyboardType = KeyboardType.Password,
                        passwordVisible = passwordVisible,
                        imeAction = ImeAction.Done,
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Lock else Icons.Default.Lock,
                                    contentDescription = "show password"
                                )
                            }
                        }
                        )
                }
                item {
                    Spacer(modifier = Modifier.height(50.dp))

                    AppButton(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        onClick = {
                            authenticationViewModel.onEvent(RegistrationFormEvent.OnRegister)
                        }
                    ) {
                        when (signupState.status) {
                            ResultStatus.INITIAL,
                            ResultStatus.UNAUTHENTICATED,
                            ResultStatus.SUCCESS,
                            ResultStatus.ERROR -> {
                                Text(
                                    text = "Sign Up",
                                    color = Color.White
                                )
                            }

                            ResultStatus.LOADING -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        8.dp,
                                        alignment = Alignment.CenterHorizontally
                                    )
                                ) {
                                    CircularProgressIndicator()
                                    Text(
                                        text = "Creating profile...",
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}