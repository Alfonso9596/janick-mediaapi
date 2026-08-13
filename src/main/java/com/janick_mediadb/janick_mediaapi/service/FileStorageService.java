package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.enums.UploadMediaType;
import com.janick_mediadb.janick_mediaapi.exception.FileDownloadException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {

    void init();

    void uploadPoster(MultipartFile file, String title, String artist, String year, UploadMediaType mediaType);

    void uploadFile(MultipartFile file, String mediaId, UploadMediaType mediaType);

    Resource load(String filename) throws FileDownloadException;

    Stream<Path> loadAll();
}