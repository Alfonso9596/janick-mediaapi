package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.enums.UploadMediaType;
import com.janickmediadb.janickmediaapi.exception.BadRequestException;
import com.janickmediadb.janickmediaapi.exception.FileDownloadException;
import com.janickmediadb.janickmediaapi.exception.InternalServerException;
import com.janickmediadb.janickmediaapi.utils.NamingUtility;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final MovieService movieService;

    private final SeriesService seriesService;

    private final GameService gameService;

    private final MusicService musicService;

    private final RecipeService recipeService;

    public static final String POSTER_FILE_TYPE = ".jpg";

    // TODO: Move resource paths to application context
    private final Path root = Paths.get("uploads");
    public static final Path movies = Paths.get("uploads/movies");
    public static final Path series = Paths.get("uploads/series");
    public static final Path games = Paths.get("uploads/games");
    public static final Path music = Paths.get("uploads/music");
    public static final Path recipes = Paths.get("uploads/recipes");

    @Autowired
    public FileStorageServiceImpl(MovieService movieService, SeriesService seriesService, GameService gameService, MusicService musicService, RecipeService recipeService) {
        this.movieService = movieService;
        this.seriesService = seriesService;
        this.gameService = gameService;
        this.musicService = musicService;
        this.recipeService = recipeService;
    }

    @Override
    public void init() {
        try {
            LOGGER.info("Creating directory uploads");
            Files.createDirectories(movies);
            Files.createDirectories(series);
            Files.createDirectories(games);
            Files.createDirectories(music);
            Files.createDirectories(recipes);
        } catch (IOException _) {
            throw new InternalServerException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void uploadPoster(MultipartFile file, String title, String artist, String year, UploadMediaType mediaType) {
        if (mediaType.equals(UploadMediaType.MUSIC)) {
            title = NamingUtility.renameTitleForMusicFilepath(title, artist, year);
        } else if (mediaType.equals(UploadMediaType.RECIPE)) {
            title = NamingUtility.returnCleanFilepathValue(title);
        } else {
            title = NamingUtility.renameTitleForFilepath(title, year);
        }
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

    @Override
    public void uploadFile(MultipartFile file, String mediaId, UploadMediaType mediaType) {
        MediaService mediaService = mediaServiceResolver(mediaType);
        if (mediaService == null) {
            throw new InternalServerException("Media service for MediaType [" + mediaType.name() + "] not found!");
        }
        Path filePath = mediaService.getMediaFilePath(Integer.parseInt(mediaId));
        LOGGER.info("Resolved to {}", filePath);
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
            Files.copy(file.getInputStream(), filePath.resolve(file.getOriginalFilename()));
        } catch (FileAlreadyExistsException _) {
            throw new BadRequestException("A file with the name " + filePath + " already exists");
        } catch (IOException _) {
            throw new InternalServerException("Could not save file!");
        }
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

    private Path createPathFromType(UploadMediaType mediaType, String filename) {
        return switch (mediaType) {
            case GAME -> games.resolve(filename);
            case MOVIE -> movies.resolve(filename);
            case SERIES -> series.resolve(filename);
            case MUSIC -> music.resolve(filename);
            case RECIPE -> recipes.resolve(filename);
        };
    }

    private MediaService mediaServiceResolver(UploadMediaType mediaType) {
        switch (mediaType) {
            case MOVIE -> {
                return movieService;
            }
            case SERIES -> {
                return seriesService;
            }
            case GAME -> {
                return gameService;
            }
            case MUSIC -> {
                return musicService;
            }
            case RECIPE -> {
                return recipeService;
            }
            default -> {
                return null;
            }
        }
    }
}
