package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.auth.UserDetailsImpl;
import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MovieRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.InternalServerException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.exception.UnauthorizedException;
import com.janick_mediadb.janick_mediaapi.input.GenreInput;
import com.janick_mediadb.janick_mediaapi.input.MovieInput;
import com.janick_mediadb.janick_mediaapi.model.FileInfoModel;
import com.janick_mediadb.janick_mediaapi.model.MovieModel;
import com.janick_mediadb.janick_mediaapi.model.RatingUpdateModel;
import com.janick_mediadb.janick_mediaapi.model.response.MovieResponse;
import com.janick_mediadb.janick_mediaapi.model.response.MovieSearchCriteria;
import com.janick_mediadb.janick_mediaapi.model.specifications.MovieSpecification;
import com.janick_mediadb.janick_mediaapi.repository.MovieRepository;
import com.janick_mediadb.janick_mediaapi.utils.FileUtility;
import com.janick_mediadb.janick_mediaapi.utils.NamingUtility;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;

@Service
public class MovieService implements MediaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);
    private static final String MOVIE_FILES_PATH = "movies/";
    public static final String MOVIE_WITH_ID_DOES_NOT_EXIST = "Movie with id {0} does not exist";

    private final MovieRepository movieRepository;

    private final MovieGenreXrefService movieGenreXrefService;

    private final MovieRatingXrefService movieRatingXrefService;

    private final MovieGenreService genreService;

    private final UserService userService;

    @Autowired
    public MovieService(MovieRepository movieRepository, MovieGenreXrefService movieGenreXrefService, MovieRatingXrefService movieRatingXrefService, MovieGenreService genreService, UserService userService) {
        this.movieRepository = movieRepository;
        this.movieGenreXrefService = movieGenreXrefService;
        this.movieRatingXrefService = movieRatingXrefService;
        this.genreService = genreService;
        this.userService = userService;
    }

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
                GenreInput input = new GenreInput();
                input.setName(genre);
                try {
                    genreService.saveGenre(input);
                } catch (BadRequestException _) {
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

        String filename = NamingUtility.renameTitleForFilepath(movieInput.getName(), movieInput.getYear());

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

    public MovieModel updateMovie(int id, MovieInput movieInput) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersEntity user = userService.getUserByUsername(userDetails.getUsername());

        MovieEntity movieEntity = movieRepository.findById(id).orElseThrow(() -> new NotFoundException("Movie with id " + id + " not found!"));

        if (movieEntity.getUser().getId() != user.getId() && user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN"))) {
            throw new UnauthorizedException("User " + user.getUsername() + " is not allowed to update movie with id " + id);
        }

        List<MovieGenreEntity> genreEntities = new ArrayList<>();
        if (!movieInput.getGenres().isEmpty()) {
            for (String genre : movieInput.getGenres()) {
                GenreInput input = new GenreInput();
                input.setName(genre);
                try {
                    genreService.saveGenre(input);
                } catch (BadRequestException _) {
                    LOGGER.warn("Genre {} already exists", genre);
                }
            }

            for (String genre : movieInput.getGenres()) {
                MovieGenreEntity movieGenreEntity = genreService.getGenreByName(genre);
                genreEntities.add(movieGenreEntity);
            }
        }
        movieGenreXrefService.deleteMovieGenreReferenceByMovieId(id);
        mapToEntity(movieEntity, movieInput);

        Instant currentTime = Instant.now();
        movieEntity.setLastUpdated(currentTime);
        movieEntity = movieRepository.save(movieEntity);
        LOGGER.info("updateMovie: Updating movie {}", movieEntity.toModel());
        movieGenreXrefService.saveMovieGenreXref(movieEntity, genreEntities);

        return movieEntity.toModel();
    }

    private void mapToEntity(MovieEntity existingMovie, MovieInput movieInput) {
        if (movieInput.getName() != null && !movieInput.getName().isEmpty()) {
            existingMovie.setName(movieInput.getName());
        }
        if (movieInput.getYear() != null && !movieInput.getYear().isEmpty()) {
            existingMovie.setYear(movieInput.getYear());
        }
        if (movieInput.getDescription() != null && !movieInput.getDescription().isEmpty()) {
            existingMovie.setDescription(movieInput.getDescription());
        }
        if (movieInput.getLength() != null) {
            existingMovie.setLength(movieInput.getLength());
        }
    }

    public ResponseEntity<String> deleteMovie(int id) {
        Optional<MovieEntity> opMovie = movieRepository.findById(id);
        if (opMovie.isEmpty()) {
            String message = MessageFormat.format(MOVIE_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        MovieEntity movieEntity = opMovie.get();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId() != movieEntity.getUser().getId() && principal.getAuthorities().stream().noneMatch(role -> Objects.equals(role.getAuthority(), "ADMIN"))) {
            throw new UnauthorizedException("User " + principal.getUsername() + " is not allowed to delete movie " + movieEntity.getName());
        }

        movieGenreXrefService.deleteMovieGenreReferenceByMovieId(id);
        movieRatingXrefService.deleteMovieRatingReferenceByMovieId(id);

        LOGGER.info("deleteMovie: Deleting movie {}", movieEntity.toModel());
        movieRepository.delete(movieEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MessageFormat.format("The movie {0} has been deleted", movieEntity.getName()));
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
        String fileStorageName = NamingUtility.renameTitleForFilepath(movieEntity.getName(), movieEntity.getYear());
        Path filePath = FileStorageServiceImpl.movies.resolve(fileStorageName).resolve("files");

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
        } catch (IOException e) {
            throw new InternalServerException("Could not create directory " + filePath, e);
        }
        List<FileInfoModel> fileInfoModels = FileUtility.getDirList(filePath.toFile());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfoModels);
    }

    @Override
    public Path getMediaFilePath(int mediaId) {
        Optional<MovieEntity> opMovie = movieRepository.findById(mediaId);
        if (opMovie.isEmpty()) {
            String message = MessageFormat.format(MOVIE_WITH_ID_DOES_NOT_EXIST, mediaId);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        MovieEntity movieEntity = opMovie.get();
        String fileStorageName = NamingUtility.renameTitleForFilepath(movieEntity.getName(), movieEntity.getYear());
        return FileStorageServiceImpl.movies.resolve(fileStorageName).resolve("files");
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
}
