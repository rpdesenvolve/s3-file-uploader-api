package br.com.rpdesenvolve.s3_file_uploader_api.domain.port

interface S3StoragePort {
    fun upload(fileName: String, content: ByteArray)
    fun generatePresignedUrl(filename: String): String
}