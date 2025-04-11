package com.tanu.spotifymusic.domain.model.response

data class SpotifyAuthRes(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String
)

