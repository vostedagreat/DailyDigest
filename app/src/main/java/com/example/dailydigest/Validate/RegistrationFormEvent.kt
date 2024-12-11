package com.example.dailydigest.Validate

sealed class RegistrationFormEvent {
    data class NameChanged(val name: String): RegistrationFormEvent()
    data class EmailChanged(val email: String): RegistrationFormEvent()
    data class PasswordChanged(val password: String): RegistrationFormEvent()
    data class ConfirmPassword(
        val confirmPassword: String
    ): RegistrationFormEvent()

    data object OnRegister: RegistrationFormEvent()
    data object OnLogin: RegistrationFormEvent()
}