package com.squrlabs.sca.web.dto.chat

import com.squrlabs.sca.domain.model.chat.ContentType
import java.util.*

data class MessageDto(val id:String?,
                      val senderId: String,
                      val conversationId: String,
                      val content: String,
                      val mediaUrl: String,
                      val contentType: ContentType,
                      val createdAt: Date,
                      val updatedAt: Date,
                      val receivedAt: Date? = null,
                      val readAt: Date? = null)