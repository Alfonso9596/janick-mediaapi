package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.enums.UploadMediaType;
import com.janickmediadb.janickmediaapi.exception.FileDownloadException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    void init();

    void uploadPoster(MultipartFile file, String title, String artist, String year, UploadMediaType mediaType);

    void uploadFile(MultipartFile file, String mediaId, UploadMediaType mediaType);

    Resource load(String filename) throws FileDownloadException;

    Stream<Path> loadAll();
}