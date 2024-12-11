package com.example.dailydigest.Validate

class ValidateUsername {

    fun execute(name: String): ValidationResult {
        if(name.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Username can't be blank"
            )
        }
        if(name.length < 4){
            return ValidationResult(
                successful = false, errorMessage = "That's not a valid username"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}