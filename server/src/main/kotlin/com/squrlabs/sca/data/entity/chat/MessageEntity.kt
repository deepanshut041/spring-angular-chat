package com.squrlabs.sca.data.entity.chat

import com.squrlabs.sca.domain.model.chat.ContentType
import com.squrlabs.sca.domain.model.chat.MessageModel
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("messages")
data class MessageEntity(
        @Id val id:String?,
        val senderId: String,
        @Indexed(unique = false) val conversationId: String,
        val content: String,
        val files: List<FileEntity>,
        val contentType: ContentType,
        val createdAt: Date,
        val updatedAt: Date,
        val read: Boolean
)

object MessageMapper {
    fun to(entity: MessageEntity) = MessageModel(
            id = entity.id!!,
            senderId = entity.senderId,
            chatId = entity.conversationId,
            content = entity.content,
            files = entity.files.map { FileMapper.to(it) },
            contentType = entity.contentType,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            read = entity.read
    )
}