package com.mE.Health.data.model.apiResponse

import com.mE.Health.data.model.apiRequest.Message

data class ChatResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val message: Message,
    val index: Int
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)