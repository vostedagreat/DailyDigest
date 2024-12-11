package com.example.dailydigest.Validate

class ValidateConfirmPassword {
    fun execute(password: String, confirmPassword: String): ValidationResult {
        if(password != confirmPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password doesn't match"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}