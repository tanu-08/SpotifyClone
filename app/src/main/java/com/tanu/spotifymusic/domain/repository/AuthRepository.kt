package com.tanu.spotifymusic.domain.repository

import com.tanu.spotifymusic.common.AppConstants
import com.tanu.spotifymusic.data.remote.ApiService
import com.tanu.spotifymusic.domain.model.response.SpotifyAuthRes
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService){

    suspend fun getAccessToken(code: String, redirectUri: String): Response<SpotifyAuthRes> {
        val authHeader = "Basic " + android.util.Base64.encodeToString(
            "${AppConstants.CLIENT_ID}:${AppConstants.CLIENT_SECRET}".toByteArray(), android.util.Base64.NO_WRAP
        )
        return apiService.getAccessToken(authHeader, code = code, redirectUri = redirectUri)
    }

}