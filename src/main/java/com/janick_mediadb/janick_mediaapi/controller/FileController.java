package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.enums.UploadMediaType;
import com.janick_mediadb.janick_mediaapi.exception.FileDownloadException;
import com.janick_mediadb.janick_mediaapi.model.FileInfoModel;
import com.janick_mediadb.janick_mediaapi.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/api/files/uploadPoster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPoster(
            @RequestPart("filename") String title,
            @RequestPart("year") String year,
            @RequestParam("mediaType") UploadMediaType mediaType,
            @RequestPart("file") MultipartFile file) {
        try {
            fileStorageService.save(file, title, year, mediaType);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Uploaded the poster successfully: " + title);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body("Could not upload the poster: " + title + ". Error: " + e.getMessage());
        }
    }

    @GetMapping(value = "/api/files", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FileInfoModel>> getListFiles() {
        List<FileInfoModel> fileInfoModels = fileStorageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String relativePath = path.toString().replace("uploads\\", "");
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile", relativePath).build().toString();
            return new FileInfoModel(filename, url.replace("\\", "/"));
        }).collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileInfoModels);
    }

    @GetMapping(value = "/api/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> getFile(
            @RequestParam String filename) throws FileDownloadException {
        LOGGER.info("FILENAME: " + filename);
        Resource file = fileStorageService.load(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
