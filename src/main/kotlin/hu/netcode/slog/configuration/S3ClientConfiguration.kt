package hu.netcode.slog.configuration

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import hu.netcode.slog.properties.S3Properties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3ClientConfiguration(
    private val s3Properties: S3Properties
) {
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
}