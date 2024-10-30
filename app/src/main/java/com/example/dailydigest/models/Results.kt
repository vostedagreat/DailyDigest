package domain.models

import com.example.dailydigest.dto.Articles

data class Results<out T>(val status: ResultStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> initial(): Results<T> {
            return Results(ResultStatus.INITIAL, null, null)
        }

        fun <T> success(data: T?): Results<T & Any> {
            return Results(ResultStatus.SUCCESS, data, null)
        }

        fun <T> error(msg: String = "Something went wrong. Try again"): Results<T> {
            return Results(ResultStatus.ERROR, null, msg)
        }

        fun <T> unauthenticated(): Results<T> {
            return Results(ResultStatus.UNAUTHENTICATED, null, null)
        }

        fun <T> loading(): Results<T> {
            return Results(ResultStatus.LOADING, null, null)
        }
    }
}
enum class ResultStatus {
    INITIAL,
    SUCCESS,
    ERROR,
    LOADING,
    UNAUTHENTICATED
}
