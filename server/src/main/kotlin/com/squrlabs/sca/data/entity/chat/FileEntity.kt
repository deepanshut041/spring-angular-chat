package com.squrlabs.sca.data.entity.chat

import com.squrlabs.sca.domain.model.chat.FileModel

data class FileEntity(val url: String, val type: String)

object FileMapper {
    fun to(entity: FileEntity) = FileModel(url = entity.url, type = entity.type)
    fun from(model: FileModel) = FileEntity(url = model.url, type = model.type)
}