package com.squrlabs.sca.domain.model.chat


import java.util.*

data class MessageModel(
        val id: String?,
        val senderId: String,
        val chatId: String,
        val content: String,
        val files: List<FileModel>,
        val contentType: ContentType,
        val createdAt: Date,
        val updatedAt: Date,
        val read: Boolean
)

data class FileModel(val url: String, val type: String)