package domain.models
data class Results<out T>(val status: ResultStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> initial(): Results<T> {
            return Results(ResultStatus.INITIAL, null, null)
        }
        fun <T> success(data: T?): Results<T & Any> {
            return Results(ResultStatus.SUCCESS, data, null)
        }

        fun <T> error(msg: String): Results<T> {
            return Results(ResultStatus.ERROR, null, msg)
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
}