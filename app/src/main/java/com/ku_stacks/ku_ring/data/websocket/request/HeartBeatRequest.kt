package com.ku_stacks.ku_ring.data.websocket.request

import com.google.gson.annotations.SerializedName

data class HeartBeatRequest(
    @SerializedName(value = "type")
    val type: String,
    @SerializedName(value = "content")
    val content: String
)
