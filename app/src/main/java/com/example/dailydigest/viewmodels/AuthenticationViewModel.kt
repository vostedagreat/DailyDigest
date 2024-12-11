package com.example.dailydigest.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailydigest.Validate.RegistrationFormEvent
import com.example.dailydigest.Validate.ValidateConfirmPassword
import com.example.dailydigest.Validate.ValidateEmail
import com.example.dailydigest.Validate.ValidatePassword
import com.example.dailydigest.Validate.ValidateUsername
import com.example.dailydigest.state.RegistrationFormState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import domain.models.Results
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel: ViewModel() {

    private val validateEmail: ValidateEmail = ValidateEmail()
    private val validatePassword: ValidatePassword = ValidatePassword()
    private val validateUsername: ValidateUsername = ValidateUsername()
    private val validateConfirmPassword: ValidateConfirmPassword = ValidateConfirmPassword()

    private val auth: FirebaseAuth = Firebase.auth

    val loginState = MutableStateFlow(
        Results.initial<Boolean>()
    )
    val signUpState = MutableStateFlow(
        Results.initial<Boolean>()
    )

    var state by mutableStateOf(RegistrationFormState())

    private fun loginWithEmailAndPassword() {
        loginState.value = Results.loading()

        auth.signInWithEmailAndPassword(state.email, state.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    Log.d("Authentication", "signInWithEmailAndPassword:success $user")
                    loginState.value = Results.success(task.isSuccessful)
                } else {
                    // Handle login failure (e.g., display error message)
                    Log.w("Authentication", "signInWithEmailAndPassword:failure", task.exception)
                    loginState.value = Results.error(task.exception?.message ?: "Login failed. try again" )
                }
            }
    }





    fun onEvent(event: RegistrationFormEvent){
        when(event) {
            is RegistrationFormEvent.EmailChanged -> {
                state = state.copy(email = event.email.trim())
            }
            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegistrationFormEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }
            is RegistrationFormEvent.ConfirmPassword -> {
                state = state.copy(confirmPassword = event.confirmPassword)
            }

            RegistrationFormEvent.OnLogin -> {
                val emailResult = validateEmail.execute(state.email)
                val passwordResult = validatePassword.execute(state.password)


                val hasError = listOf(
                    emailResult,
                    passwordResult
                ).any { !it.successful }

                if(hasError) {
                    state = state.copy(
                        emailError = emailResult.errorMessage,
                        passwordError = passwordResult.errorMessage,
                    )
                    return
                }
                loginWithEmailAndPassword()
            }


            RegistrationFormEvent.OnRegister -> {
                val emailResult = validateEmail.execute(state.email)
                val passwordResult = validatePassword.execute(state.password)
                val usernameResult = validateUsername.execute(state.name)
                val confirmPasswordResult = validateConfirmPassword.execute(
                    state.password, state.confirmPassword
                )

                val hasError = listOf(
                    emailResult,
                    passwordResult,
                    usernameResult,
                    confirmPasswordResult
                ).any { !it.successful }

                if(hasError) {
                    state = state.copy(
                        emailError = emailResult.errorMessage,
                        passwordError = passwordResult.errorMessage,
                        nameError = usernameResult.errorMessage,
                        confirmPasswordError = confirmPasswordResult.errorMessage
                    )
                    return
                }
                submitData()
            }
        }
    }

    private fun submitData() {
        viewModelScope.launch {

            signUpState.value = Results.loading()

            auth.createUserWithEmailAndPassword(state.email, state.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        Log.d("Authentication", "createUserWithEmailAndPassword:success $user")
                        signUpState.value = Results.success(task.isSuccessful)
                    } else {
                        // Handle registration failure (e.g., display error message)
                        Log.w("Authentication", "createUserWithEmailAndPassword:failure", task.exception)
                        signUpState.value = Results.error(task.exception?.message ?: "Registration failed. Try again.")
                    }
                }
        }
    }

    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }
}