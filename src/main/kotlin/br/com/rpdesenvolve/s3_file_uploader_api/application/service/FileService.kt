package br.com.rpdesenvolve.s3_file_uploader_api.application.service

import br.com.rpdesenvolve.s3_file_uploader_api.domain.port.S3StoragePort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class FileService(
    private val s3StorePort: S3StoragePort
) {
    fun upload(file: MultipartFile): String {
        val fileName = file.originalFilename ?: UUID.randomUUID().toString()
        s3StorePort.upload(fileName, file.bytes)
        return fileName
    }

    fun generatePresignedUrl(fileName: String): String {
        return s3StorePort.generatePresignedUrl(fileName)
    }
}