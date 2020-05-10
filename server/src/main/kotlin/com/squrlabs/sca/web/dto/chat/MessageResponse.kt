package com.squrlabs.sca.web.dto.chat

import com.squrlabs.sca.domain.model.chat.ContentType
import com.squrlabs.sca.domain.model.chat.FileModel
import com.squrlabs.sca.domain.model.chat.MessageModel
import java.util.*

data class MessageResponse(val id:String,
                           val senderId: String,
                           val chatId: String,
                           val content: String,
                           val files: List<FileResponse>,
                           val contentType: ContentType,
                           val createdAt: Date,
                           val updatedAt: Date,
                           val read: Boolean)

data class FileResponse(
        val url: String,
        val type: String
)

object MessageResponseMapper{
    fun from(model: MessageModel) = MessageResponse(
            id = model.id!!,
            senderId = model.senderId,
            chatId = model.chatId,
            content = model.content,
            files = model.files.map { FileResponse(it.url, it.type) },
            contentType = model.contentType,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt,
            read = model.read
    )
}