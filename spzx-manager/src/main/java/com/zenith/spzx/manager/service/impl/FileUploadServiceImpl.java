package com.zenith.spzx.manager.service.impl;

import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.manager.properties.MinioProperties;
import com.zenith.spzx.manager.service.FileUploadService;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.utils.FileUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) {
        boolean found = false;
        String url=null;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("spzx-bucket").build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("spzx-bucket").build());
            }

            String objectName= FileUtils.generateFileName(file.getOriginalFilename());
            log.debug("Upload file ...: {}",objectName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .stream(file.getInputStream(),file.getSize(),-1)
                            .build()
            );
            url= minioProperties.getHost()+"/"+minioProperties.getBucket()+"/"+objectName;
            log.debug("Upload completed, request url: {}",url);
        } catch (Exception e) {
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }

        return url;
    }
}
