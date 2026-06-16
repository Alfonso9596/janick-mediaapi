package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.enums.UploadMediaType;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.FileDownloadException;
import com.janick_mediadb.janick_mediaapi.exception.InternalServerException;
import com.janick_mediadb.janick_mediaapi.utils.NamingUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    public static final String POSTER_FILE_TYPE = ".jpg";

    // TODO: Move resource paths to application context
    private final Path root = Paths.get("uploads");
    public static final Path movies = Paths.get("uploads/movies");
    private final Path series = Paths.get("uploads/series");
    private final Path games = Paths.get("uploads/games");

    @Override
    public void init() {
        try {
            LOGGER.info("Creating directory uploads");
            Files.createDirectories(movies);
            Files.createDirectories(series);
            Files.createDirectories(games);
        } catch (IOException _) {
            throw new InternalServerException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file, String title, String year, UploadMediaType mediaType) {
        title = NamingUtility.renameTitleForFilepath(title) + "_" + year;
        Path path = createPathFromType(mediaType, title);
        LOGGER.info("Resolved to {}", path);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Files.copy(file.getInputStream(), path.resolve(title + POSTER_FILE_TYPE));
        } catch (FileAlreadyExistsException _) {
            throw new BadRequestException("A file with the name " + path + " already exists");
        } catch (IOException _) {
            throw new InternalServerException("Could not save file!");
        }
    }

    private Path createPathFromType(UploadMediaType mediaType, String filename) {
        return switch (mediaType) {
            case GAME -> this.games.resolve(filename);
            case MOVIE -> this.movies.resolve(filename);
            case SERIES -> this.series.resolve(filename);
        };
    }

    @Override
    public Resource load(String filename) throws FileDownloadException {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileDownloadException("Could not read the file " + filename);
            }
        } catch (MalformedURLException e) {
            throw new BadRequestException("Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root).filter(Files::isRegularFile);
        } catch (IOException _) {
            throw new InternalServerException("Could not load the files!");
        }
    }

}
