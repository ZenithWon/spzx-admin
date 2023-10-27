package com.zenith.spzx.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {
    private String host;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
