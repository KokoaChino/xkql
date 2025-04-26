package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.convert.WatermarkParamsConvert;
import com.example.dto.ImageRequestDTO;
import com.example.dto.WatermarkDataDTO;
import com.example.dto.WatermarkParamsDTO;
import com.example.exception.FontNotFoundException;
import com.example.service.api.BatchImageWatermarkerService;
import com.example.util.ImageUtil;
import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


@RestController
@RequestMapping("/api/batch-image-watermarker")
public class BatchImageWatermarkerController {

    @Resource
    BatchImageWatermarkerService service;


    @PostMapping("/start-task")
    public void startTask(@RequestParam("username") String username,
                          @RequestParam("file") MultipartFile file,
                          @RequestParam("params") String json) throws IOException {
        WatermarkParamsDTO params = JSON.parseObject(json, new TypeReference<WatermarkParamsDTO>() {});
        service.startTask(username, file.getBytes(), WatermarkParamsConvert.toEntity(params));
    }

    @GetMapping("/get-progress")
    Integer getProgress(@RequestParam("username") String username) {
        return service.getProgress(username);
    }

    @GetMapping("/get-zip-file")
    ResponseEntity<InputStreamResource> getZipFile(@RequestParam("username") String username) {
        return service.getZipFile(username);
    }

    @PostMapping("/stop-task")
    void stopTask(@RequestParam("username") String username) {
        service.stopTask(username);
    }

    @GetMapping("/get-additional-data")
    public Map<String, Set<String>> getAdditionalData(@RequestParam("username") String username) {
        return service.getAdditionalData(username);
    }


    @GetMapping("/get-preset-style-params")
    public List<WatermarkParamsDTO> getPresetStyleParams() {
        return service.getPresetStyleParams();
    }

    @GetMapping("/get-custom-style-params")
    public List<WatermarkDataDTO> getCustomStyleParams(@RequestParam("username") String username) {
        return service.getCustomStyleParams(username);
    }

    @PostMapping("/add-custom-style-params")
    public void addCustomStyleParams(@RequestBody WatermarkDataDTO data) {
        service.addCustomStyleParams(data);
    }

    @PostMapping("/delete-custom-style-params")
    public void deleteCustomStyleParams(@RequestParam("id") Integer id) {
        service.deleteCustomStyleParams(id);
    }

    @PostMapping("/update-custom-style-params")
    public void updateCustomStyleParams(@RequestBody WatermarkDataDTO data) {
        service.updateCustomStyleParams(data);
    }


    @GetMapping("/get-default-background-image")
    public String getDefaultBackgroundImage() {
        return service.getDefaultBackgroundImage();
    }

    @PostMapping("/get-preview-image")
    public String getModifiedImage(@RequestBody ImageRequestDTO request) throws FontNotFoundException {
        WatermarkParamsDTO params = request.getParams();
        String base64 = request.getBase64();
        return service.getModifiedImage(
                WatermarkParamsConvert.toEntity(params),
                WatermarkParamsConvert.toBytes(base64)
        );
    }


    @PostMapping("/check-font")
    public Boolean checkFont(@RequestParam("fontName") String fontName) {
        return ImageUtil.getFont(fontName, 0, 0) != null;
    }

    @PostMapping("/handle-upload-ttf")
    public String handleUploadFontFile(@RequestParam("fontFile") MultipartFile file) throws IOException {
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename()).replaceAll("[^a-zA-Z0-9.-]", "_");
        return service.handleUploadFontFile(file.getBytes(), originalFileName);
    }
}