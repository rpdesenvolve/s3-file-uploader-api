package br.com.rpdesenvolve.s3_file_uploader_api.infrastructure.s3

import br.com.rpdesenvolve.s3_file_uploader_api.domain.port.S3StoragePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.time.Duration

@Component
class S3StorageAdapter(
    @Value("\${aws.s3.bucket}")
    private val bucket: String,
    private val s3Client: S3Client
): S3StoragePort {

    override fun upload(fileName: String, content: ByteArray) {
        val putRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .build()
        s3Client.putObject(putRequest, RequestBody.fromBytes(content))
    }

    override fun generatePresignedUrl(filename: String): String {
        val request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(filename)
            .build()
        val presigner = S3Presigner.create()
        val presignedRequest = presigner.presignGetObject {
            it.signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(request)
        }
        return presignedRequest.url().toString()
    }
}