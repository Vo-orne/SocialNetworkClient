package com.example.myprofile.domain.states

/**
 * A sealed class representing the state of an API request.
 * It can be in one of four states: Initial, Success, Error, or Loading.
 */
sealed class ApiState {

    /**
     * Represents the initial state of an API request.
     */
    data object Initial : ApiState()

    /**
     * Represents a successful API request.
     * Contains the data returned from the API.
     *
     * @param T The type of the data.
     * @property data The data returned from the API.
     */
    data class Success<T>(val data: T) : ApiState()

    /**
     * Represents an error occurred during an API request.
     * Contains the error message describing what went wrong.
     *
     * @property error A string containing the error message.
     */
    data class Error(val error: String) : ApiState()

    /**
     * Represents the loading state of an API request.
     * Indicates that the request is in progress.
     */
    data object Loading : ApiState()
}
