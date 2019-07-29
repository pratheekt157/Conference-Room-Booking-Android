package com.example.conferencerommapp.model

data class RefreshToken(
    var accessToken: String? = null,
    var refreshToken: String? = null
)