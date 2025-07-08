package br.com.rpdesenvolve.s3_file_uploader_api.adapter.controller

import br.com.rpdesenvolve.s3_file_uploader_api.application.service.FileService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/v1/files")
class FileController(
    private val fileService: FileService
) {
    private val log = LoggerFactory.getLogger(FileController::class.java)

    @PostMapping
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<Map<String, String>> {
        log.info("Received file upload request: name={}, size={} bytes", file.originalFilename, file.size)

        if (file.isEmpty) {
            log.warn("Upload failed: file is empty")
            return ResponseEntity.badRequest().body(mapOf("error" to "Empty file not allowed"))
        }

        return try {
            val url = fileService.upload(file)
            log.info("File uploaded successfully: {}", url)
            ResponseEntity.status(HttpStatus.CREATED).body(mapOf("url" to url))
        } catch (ex: Exception) {
            log.error("Upload failed: {}", ex.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Failed to upload file"))
        }
    }

    @GetMapping("/{filename}")
    fun downloadUrl(@PathVariable filename: String) =
        ResponseEntity.ok(fileService.generatePresignedUrl(filename))
}