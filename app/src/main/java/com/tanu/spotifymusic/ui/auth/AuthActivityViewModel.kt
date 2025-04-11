package com.tanu.spotifymusic.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanu.spotifymusic.domain.model.response.SpotifyAuthRes
import com.tanu.spotifymusic.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthActivityViewModel  @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _authResponse = MutableStateFlow<SpotifyAuthRes?>(null)
    val authResponse = _authResponse.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun authenticateUser(code: String, redirectUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                val response = repository.getAccessToken(code, redirectUri)
                if (response.isSuccessful) {
                    emit(response.body())
                } else {
                    throw Exception("Error: ${response.message()}")
                }
            }
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { result ->
                    _authResponse.value = result
                }
        }
    }
}
