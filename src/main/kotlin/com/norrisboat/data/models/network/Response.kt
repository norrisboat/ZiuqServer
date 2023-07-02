package com.norrisboat.data.models.network

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(val success: Boolean, val message: String, val data: T?)