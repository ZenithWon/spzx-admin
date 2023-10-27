package com.zenith.spzx.manager.config;

import com.zenith.spzx.manager.properties.MinioProperties;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class BeanFactoryConfig {
    @Autowired
    private MinioProperties minioProperties;

    //RedisTemplate
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Object>template=new RedisTemplate<>();
        //关联
        template.setConnectionFactory(factory);
        //设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        //设置value的序列化器
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    //MinioClient
    @Bean
    public MinioClient minioClient(){
        MinioClient minioClient=MinioClient.builder()
                .endpoint(minioProperties.getHost())
                .credentials(minioProperties.getAccessKey() , minioProperties.getSecretKey())
                .build();

        return minioClient;
    }
}
