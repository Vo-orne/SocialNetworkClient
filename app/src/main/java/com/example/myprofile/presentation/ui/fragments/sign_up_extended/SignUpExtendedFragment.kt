package com.example.myprofile.presentation.ui.fragments.sign_up_extended

import androidx.fragment.app.viewModels
import com.example.myprofile.databinding.FragmentSingUpExtendedBinding
import com.example.myprofile.domain.ApiService
import com.example.myprofile.data.model.UserResponse
import com.example.myprofile.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class SignUpExtendedFragment : BaseFragment<FragmentSingUpExtendedBinding>(FragmentSingUpExtendedBinding::inflate) {

    override fun setListeners() {
        TODO("Not yet implemented")
    }

    fun registerUser() {
        val apiService: ApiService by viewModels()

        val email = "test@email"
        val password = "112233"

        val call: Call<UserResponse> = apiService.registerUser(
            email,
            password,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null) {
                        // Successful registration - get user data and access tokens
                        val user = userResponse.user
                        val accessToken = userResponse.accessToken
                        val refreshToken = userResponse.refreshToken

                        // Тепер ви можете використовувати ці дані для подальших дій
                    }
                } else {
                    // Error processing from the server (for example, invalid data in the request)
                    val errorBody = response.errorBody()?.string()
                    // Тут ви можете обробити текст помилки, який повертає сервер
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Обробка помилки зв'язку
            }
        })
    }
}