package com.janickmediadb.janickmediaapi.controller;

import com.janickmediadb.janickmediaapi.enums.DownloadFileType;
import com.janickmediadb.janickmediaapi.enums.UploadMediaType;
import com.janickmediadb.janickmediaapi.exception.FileDownloadException;
import com.janickmediadb.janickmediaapi.model.FileInfoModel;
import com.janickmediadb.janickmediaapi.service.FileStorageService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/api/files/uploadPoster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPoster(
            @RequestPart("filename") String title,
            @RequestPart(name = "artist", required = false) String artist,
            @RequestPart(name = "year", required = false) String year,
            @RequestParam("mediaType") UploadMediaType mediaType,
            @RequestPart("file") MultipartFile file) {
        try {
            fileStorageService.uploadPoster(file, title, artist, year, mediaType);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Uploaded the poster successfully: " + title);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body("Could not upload the poster: " + title + ". Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/api/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart("mediaId") String mediaId,
            @RequestParam("mediaType") UploadMediaType mediaType,
            @RequestPart("file") MultipartFile file) {
        try {
            fileStorageService.uploadFile(file, mediaId, mediaType);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Uploaded the file successfully: " + file.getName());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body("Could not upload the file: " + file.getName() + ". Error: " + e.getMessage());
        }
    }

    // TODO: Probably to be deprecated
    @GetMapping(value = "/api/files", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FileInfoModel>> getListFiles() {
        List<FileInfoModel> fileInfoModels = fileStorageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            long fileSize = path.toFile().length();
            String relativePath = path.toString().replace("uploads\\", "");
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile", relativePath).build().toString();
            return new FileInfoModel(filename, url.replace("\\", "/"), fileSize, DownloadFileType.ZIP, new ArrayList<>());
        }).toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileInfoModels);
    }

    @GetMapping(value = "/api/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> getFile(
            @RequestParam String filename) throws FileDownloadException {

        Resource file = fileStorageService.load(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
