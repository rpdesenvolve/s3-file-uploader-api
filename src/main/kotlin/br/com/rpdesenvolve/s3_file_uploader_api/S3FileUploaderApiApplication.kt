package br.com.rpdesenvolve.s3_file_uploader_api

import br.com.rpdesenvolve.s3_file_uploader_api.infrastructure.config.AwsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(AwsProperties::class)
@SpringBootApplication
class S3FileUploaderApiApplication

fun main(args: Array<String>) {
	runApplication<S3FileUploaderApiApplication>(*args)
}
