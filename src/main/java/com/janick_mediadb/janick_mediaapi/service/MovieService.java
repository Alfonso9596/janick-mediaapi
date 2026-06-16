package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.auth.UserDetailsImpl;
import com.janick_mediadb.janick_mediaapi.controller.FileController;
import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MovieRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.enums.DownloadFileType;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.InternalServerException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.input.MovieGenreInput;
import com.janick_mediadb.janick_mediaapi.input.MovieInput;
import com.janick_mediadb.janick_mediaapi.model.FileInfoModel;
import com.janick_mediadb.janick_mediaapi.model.MovieModel;
import com.janick_mediadb.janick_mediaapi.model.RatingUpdateModel;
import com.janick_mediadb.janick_mediaapi.model.response.MovieResponse;
import com.janick_mediadb.janick_mediaapi.model.response.MovieSearchCriteria;
import com.janick_mediadb.janick_mediaapi.model.specifications.MovieSpecification;
import com.janick_mediadb.janick_mediaapi.repository.MovieRepository;
import com.janick_mediadb.janick_mediaapi.utils.NamingUtility;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);
    private static final String MOVIE_FILES_PATH = "movies/";
    public static final String MOVIE_WITH_ID_DOES_NOT_EXIST = "Movie with id {0} does not exist";

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieGenreXrefService movieGenreXrefService;

    @Autowired
    private MovieRatingXrefService movieRatingXrefService;

    @Autowired
    private MovieGenreService genreService;

    @Autowired
    private UserService userService;

    public MovieResponse getAllMovies(int page, int pageSize, String sortBy, String sortDir, MovieSearchCriteria criteria) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<MovieEntity> specification = createSpecs(criteria);

        Page<MovieEntity> movies = movieRepository.findAll(specification, pageable);

        List<MovieEntity> listOfMovies = movies.getContent();
        List<MovieModel> content = listOfMovies.stream().map(m -> {
            MovieModel model = m.toModel();
            movieGenreXrefService.collectGenres(m.getId(), model);
            movieRatingXrefService.collectRatings(m.getId(), model);
            return model;
        }).toList();

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setContent(content);
        movieResponse.setPage(movies.getNumber());
        movieResponse.setPageSize(movies.getSize());
        movieResponse.setTotalElements(movies.getTotalElements());
        movieResponse.setTotalPages(movies.getTotalPages());
        movieResponse.setLast(movies.isLast());

        return movieResponse;
    }

    public List<String> getAllMovieNames() {
        List<String> movieNames = movieRepository.getAllMovieNames();
        movieNames.sort(Comparator.naturalOrder());
        return movieNames;
    }

    public MovieModel getMovieById(int id) {
        Optional<MovieEntity> opMovie = movieRepository.findById(id);
        if (opMovie.isPresent()) {
            LOGGER.info("getMovieById: Found movie with id {}", id);
            MovieModel model = opMovie.get().toModel();
            movieGenreXrefService.collectGenres(model.getId(), model);
            movieRatingXrefService.collectRatings(model.getId(), model);
            return model;
        } else {
            String message = MessageFormat.format(MOVIE_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public MovieModel saveMovie(MovieInput movieInput) throws BadRequestException {
        List<MovieEntity> movieEntities = getAllMovieEntities();
        Optional<MovieEntity> opMovie = movieEntities.stream()
                .filter(movie -> movieInput.getName().equals(movie.getName()))
                .filter(movie -> movieInput.getYear().equals(movie.getYear()))
                .findAny();

        if (opMovie.isPresent()) {
            String message = MessageFormat.format("The movie {0} is already registered", movieInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        List<MovieGenreEntity> genreEntities = new ArrayList<>();
        if (!movieInput.getGenres().isEmpty()) {
            for (String genre : movieInput.getGenres()) {
                MovieGenreInput input = new MovieGenreInput();
                input.setName(genre);
                try {
                    genreService.saveGenre(input);
                } catch (BadRequestException e) {
                    LOGGER.warn("Genre {} already exists", genre);
                }
            }

            for (String genre : movieInput.getGenres()) {
                MovieGenreEntity movieGenreEntity = genreService.getGenreByName(genre);
                genreEntities.add(movieGenreEntity);
            }
        }

        MovieEntity movieEntity = new MovieEntity();
        movieEntity.fromInput(movieInput);

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());
        movieEntity.setUser(user);

        String filename = NamingUtility.renameTitleForFilepath(movieInput.getName()) + "_" + movieInput.getYear();

        String posterFilename = MOVIE_FILES_PATH + filename + "/" + filename + FileStorageServiceImpl.POSTER_FILE_TYPE;
        movieEntity.setPosterFilepath(posterFilename);

        Instant currentTime = Instant.now();

        movieEntity.setCreatedAt(currentTime);
        movieEntity.setLastUpdated(currentTime);

        movieEntity = movieRepository.save(movieEntity);
        LOGGER.info("saveMovie: Saving movie {}", movieEntity.toModel());
        movieGenreXrefService.saveMovieGenreXref(movieEntity, genreEntities);

        return movieEntity.toModel();
    }

    public String deleteMovie(int id) {
        Optional<MovieEntity> opMovie = movieRepository.findById(id);
        if (opMovie.isEmpty()) {
            String message = MessageFormat.format(MOVIE_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        movieGenreXrefService.deleteMovieGenreReferenceByMovieId(id);
        movieRatingXrefService.deleteMovieRatingReferenceByMovieId(id);

        MovieEntity movieEntity = opMovie.get();
        LOGGER.info("deleteMovie: Deleting movie {}", movieEntity.toModel());
        movieRepository.delete(movieEntity);
        return MessageFormat.format("The movie {0} has been deleted", movieEntity.getName());
    }

    public String rateMovie(RatingUpdateModel ratingUpdateModel) {
        Optional<MovieEntity> opMovie = movieRepository.findById(ratingUpdateModel.getId());
        if (opMovie.isEmpty()) {
            String message = MessageFormat.format(MOVIE_WITH_ID_DOES_NOT_EXIST, ratingUpdateModel.getId());
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        MovieEntity movieEntity = opMovie.get();
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<MovieRatingXrefEntity> existingRating = movieRatingXrefService.findByMovieIdAndUser(movieEntity.getId(), userDetails.getId());
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());

        if (existingRating.isEmpty()) {
            movieRatingXrefService.addRating(movieEntity, user, ratingUpdateModel.getRating());
        } else {
            movieRatingXrefService.updateRating(existingRating.get(), ratingUpdateModel.getRating());
        }

        return MessageFormat.format("{0} has been rated with {1}", movieEntity.getName(), ratingUpdateModel.getRating());
    }

    public ResponseEntity<List<FileInfoModel>> getMovieFiles(int id) {
        Optional<MovieEntity> opMovie = movieRepository.findById(id);
        if (opMovie.isEmpty()) {
            String message = MessageFormat.format(MOVIE_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        MovieEntity movieEntity = opMovie.get();
        String fileStorageName = NamingUtility.renameTitleForFilepath(movieEntity.getName()) + "_" + movieEntity.getYear();
        Path filePath = FileStorageServiceImpl.movies.resolve(fileStorageName).resolve("files");

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
        } catch (IOException e) {
            throw new InternalServerException("Could not create directory " + filePath, e);
        }
        List<FileInfoModel> fileInfoModels = getDirList(filePath.toFile());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfoModels);
    }

    private FileInfoModel getNode(File node) {
        FileInfoModel fileInfoModel = new FileInfoModel();
        fileInfoModel.setName(node.getName());
        if (node.isDirectory()) {
            fileInfoModel.setFileType(DownloadFileType.FOLDER);

            List<FileInfoModel> childrenInfoModels = getDirList(node);
            fileInfoModel.setChildren(childrenInfoModels);
        } else {
            String relativePath = node.toPath().toString().replace("uploads\\", "");
            String url = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile", relativePath).build().toString();

            fileInfoModel.setUrl(url.replace("\\", "/"));
            fileInfoModel.setSize(node.length());

            String fileExtension = getFileExtension(node.getName());
            switch (fileExtension) {
                case "zip", "rar", "7z", "tar":
                    fileInfoModel.setFileType(DownloadFileType.ZIP);
                    break;
                default:
                    fileInfoModel.setFileType(DownloadFileType.VIDEO);
            }
        }
        return fileInfoModel;
    }

    private List<FileInfoModel> getDirList(File node) {
        List<FileInfoModel> fileInfoModels = new ArrayList<>();
        for (File file : node.listFiles()) {
            fileInfoModels.add(getNode(file));
        }
        return fileInfoModels;
    }

    private List<MovieEntity> getAllMovieEntities() {
        return new ArrayList<>(movieRepository.findAll());
    }

    private Specification<MovieEntity> createSpecs(MovieSearchCriteria criteria) {
        Specification<MovieEntity> spec = Specification.unrestricted();

        if (criteria.getName() != null) {
            spec = spec.and(MovieSpecification.likeName(criteria.getName()));
        }

        if (criteria.getYear() > 0) {
            spec = spec.and(MovieSpecification.equalsYear(criteria.getYear()));
        }

        if (criteria.getGenre() != null) {
            spec = spec.and(MovieSpecification.containsGenre(criteria.getGenre()));
        }

        return spec;
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex >= 0) {
            return filename.substring(dotIndex + 1);
        }
        return null;
    }
}
