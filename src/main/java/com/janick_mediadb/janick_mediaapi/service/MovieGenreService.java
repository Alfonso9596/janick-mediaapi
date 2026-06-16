package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.input.MovieGenreInput;
import com.janick_mediadb.janick_mediaapi.model.MovieGenreModel;
import com.janick_mediadb.janick_mediaapi.repository.MovieGenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieGenreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieGenreService.class);

    private static final String GENRE_NAME_NOT_EXIST = "Genre with name {0} does not exist";
    private static final String GENRE_ID_NOT_EXIST = "Genre with id {0} does not exist";
    private static final String GENRE_ALREADY_REGISTERED = "The genre {0} is already registered";

    private final MovieGenreRepository movieGenreRepository;

    private final MovieGenreXrefService movieGenreXrefService;

    private final SeriesGenreXrefService seriesGenreXrefService;

    @Autowired
    public MovieGenreService(MovieGenreRepository movieGenreRepository, MovieGenreXrefService movieGenreXrefService, SeriesGenreXrefService seriesGenreXrefService) {
        this.movieGenreRepository = movieGenreRepository;
        this.movieGenreXrefService = movieGenreXrefService;
        this.seriesGenreXrefService = seriesGenreXrefService;
    }

    public List<MovieGenreModel> getAllGenres() {
        List<MovieGenreModel> genres = new ArrayList<>();
        movieGenreRepository.findAllOrderByName().forEach(genre -> genres.add(genre.toModel()));

        LOGGER.info("getAllGenres: Found a total of {} genres.", genres.size());
        return genres;
    }

    public MovieGenreEntity getGenreByName(String name) {
        Optional<MovieGenreEntity> opGenre = movieGenreRepository.findByName(name);
        if (opGenre.isPresent()) {
            LOGGER.info("getGenreByName: Found genre with name {}", name);
            return opGenre.get();
        } else {
            String message = MessageFormat.format(GENRE_NAME_NOT_EXIST, name);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public MovieGenreModel getGenreById(int id) {
        Optional<MovieGenreEntity> opGenre = movieGenreRepository.findById(id);
        if (opGenre.isPresent()) {
            LOGGER.info("getGenreById: Found genre with id {}", id);
            return opGenre.get().toModel();
        } else {
            String message = MessageFormat.format(GENRE_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    public MovieGenreModel saveGenre(MovieGenreInput movieGenreInput) {
        List<MovieGenreEntity> genres = getAllGenreEntities();
        Optional<MovieGenreEntity> op = genres.stream()
                .filter(genreEntity -> movieGenreInput.getName().equalsIgnoreCase(genreEntity.getName()))
                .findAny();

        if (op.isPresent()) {
            String message = MessageFormat.format(GENRE_ALREADY_REGISTERED, movieGenreInput.getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        MovieGenreEntity movieGenreEntity = new MovieGenreEntity();
        movieGenreEntity.fromInput(movieGenreInput);

        LOGGER.info("saveGenre: Saving genre {}", movieGenreEntity.toModel());

        return movieGenreRepository.save(movieGenreEntity).toModel();
    }

    public String deleteGenre(int id) {
        Optional<MovieGenreEntity> opGenre = movieGenreRepository.findById(id);
        if (opGenre.isEmpty()) {
            String message = MessageFormat.format(GENRE_ID_NOT_EXIST, id);
            LOGGER.error(message);
            throw new NotFoundException(message);
        }

        List<MovieEntity> movieEntities = movieGenreXrefService.findMoviesByGenre(id);
        List<SeriesEntity> seriesEntities = seriesGenreXrefService.findSeriesByGenre(id);
        if (!movieEntities.isEmpty() || seriesEntities.isEmpty()) {
            String message = MessageFormat.format("Genre {0} cannot be deleted, because there are still movies/series with this genre", opGenre.get().getName());
            LOGGER.error(message);
            throw new BadRequestException(message);
        }

        MovieGenreEntity movieGenreEntity = opGenre.get();
        LOGGER.info("deleteGenre: Deleting genre {}", movieGenreEntity.toModel());
        movieGenreRepository.delete(movieGenreEntity);
        return MessageFormat.format("The genre {0} has been deleted", movieGenreEntity.getName());
    }

    private List<MovieGenreEntity> getAllGenreEntities() {
        return new ArrayList<>(movieGenreRepository.findAll());
    }

}
