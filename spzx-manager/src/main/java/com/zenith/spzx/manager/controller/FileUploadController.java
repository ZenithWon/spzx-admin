package com.zenith.spzx.manager.controller;

import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.common.log.enums.OperatorType;
import com.zenith.spzx.manager.service.FileUploadService;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/system")
@Tag(name = "File Upload Api")
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/fileUpload")
    @Operation(summary = "Upload a file")
    @Log(title = "file:upload",businessType = OperationType.FILE)
    public Result<String> fileUpload(@RequestParam("file") MultipartFile file){
        String url=fileUploadService.upload(file);

        return Result.success(url);
    }
}
