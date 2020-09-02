package hu.netcode.slog.result

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val value: T) : Result<T>()
    data class Failure(val error: Exception) : Result<Nothing>()

    val isFailure: Boolean
        get() = this is Failure

    val isSuccessful: Boolean
        get() = this !is Failure
}
