package hu.netcode.slog

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.github.slugify.Slugify
import hu.netcode.slog.properties.AWSProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@ConfigurationPropertiesScan
@EnableJpaRepositories
@SpringBootApplication
class SlogApplication(
    private val awsProperties: AWSProperties
) {
    @Bean
    fun bcryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun s3Client(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(
                BasicAWSCredentials(awsProperties.accessKey, awsProperties.secretKey))
            )
            .withRegion(Regions.fromName(awsProperties.region))
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
