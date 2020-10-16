package hu.netcode.slog

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.github.slugify.Slugify
import hu.netcode.slog.properties.S3Properties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@ConfigurationPropertiesScan
@EnableMongoRepositories
@SpringBootApplication
class SlogApplication(
    private val s3Properties: S3Properties
) {
    @Bean
    fun bcryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun s3Client(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withClientConfiguration(
                ClientConfiguration().apply {
                    signerOverride = "AWSS3V4SignerType"
                }
            )
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(s3Properties.accessKey, s3Properties.secretKey)
                )
            )
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    "${s3Properties.host}:${s3Properties.port}",
                    Regions.fromName(s3Properties.region).name
                )
            )
            .withPathStyleAccessEnabled(true)
            .build()
    }

    @Bean
    fun slugify(): Slugify {
        return Slugify()
    }
}

fun main(args: Array<String>) {
    runApplication<SlogApplication>(*args)
}
