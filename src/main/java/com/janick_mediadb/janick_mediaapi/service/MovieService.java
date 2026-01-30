package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.input.MovieGenreInput;
import com.janick_mediadb.janick_mediaapi.input.MovieInput;
import com.janick_mediadb.janick_mediaapi.model.MovieModel;
import com.janick_mediadb.janick_mediaapi.model.response.MovieResponse;
import com.janick_mediadb.janick_mediaapi.model.response.MovieSearchCriteria;
import com.janick_mediadb.janick_mediaapi.model.specifications.MovieSpecification;
import com.janick_mediadb.janick_mediaapi.repository.MovieRepository;
import com.janick_mediadb.janick_mediaapi.utils.NamingUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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

        String filename = NamingUtility.renameTitleForFilepath(movieInput.getName()) + "_" + movieInput.getYear();

        String posterFilename = MOVIE_FILES_PATH + filename + "/" + filename + FileStorageServiceImpl.POSTER_FILE_TYPE;
        movieEntity.setPosterFilepath(posterFilename);

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

    public String rateMovie(int id, int rating) {
        Optional<MovieEntity> opMovie = movieRepository.findById(id);
        if (opMovie.isEmpty()) {
            String message = MessageFormat.format(MOVIE_WITH_ID_DOES_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
        MovieEntity movieEntity = opMovie.get();
        movieRatingXrefService.addRating(movieEntity, rating);
        return MessageFormat.format("{0} has been rated with {1}", movieEntity.getName(), rating);
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
