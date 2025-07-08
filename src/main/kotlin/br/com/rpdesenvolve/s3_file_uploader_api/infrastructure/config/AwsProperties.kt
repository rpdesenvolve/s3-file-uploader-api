package br.com.rpdesenvolve.s3_file_uploader_api.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "aws")
class AwsProperties {
    @Value("\${aws.region}")
    lateinit var region: String
    lateinit var s3: S3Properties

    class S3Properties {
        lateinit var bucket: String
    }
}