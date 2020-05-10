package com.squrlabs.sca.domain.service.file

import com.squrlabs.sca.domain.model.chat.FileModel
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*
import kotlin.collections.ArrayList

@Service
class FileStorageServiceImpl() : FileStorageService {
    override fun store(files: List<MultipartFile>, chatId: String): List<FileModel> {
        val uploadedFiles = ArrayList<FileModel>()
        files.stream().map {
            println("Hello")
            val path = Path.of(ROOT_URL, chatId)
            Files.copy(it.inputStream,
                    path.resolve(UUID.randomUUID().toString() + it.originalFilename!!.split(".").last()),
                    StandardCopyOption.REPLACE_EXISTING)
            uploadedFiles.add(FileModel(path.toString(), it.contentType ?: ""))
        }
        return uploadedFiles
    }

    companion object {
        const val ROOT_URL = "uploads"
    }

}

interface FileStorageService {
    fun store(files: List<MultipartFile>, chatId: String): List<FileModel>
}