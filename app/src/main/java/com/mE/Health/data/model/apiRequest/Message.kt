package com.mE.Health.data.model.apiRequest

data class Message(
    val role: String,
    val content: String
)

data class ChatRequest(
    val messages: List<Message>,
    val temperature: Double
)