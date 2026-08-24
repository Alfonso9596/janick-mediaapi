package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.MovieEntity;
import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import com.janickmediadb.janickmediaapi.entity.xref.MovieRatingXrefEntity;
import com.janickmediadb.janickmediaapi.model.MovieModel;
import com.janickmediadb.janickmediaapi.repository.xref.MovieRatingXrefRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieRatingXrefService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRatingXrefService.class);

    private final MovieRatingXrefRepository movieRatingXrefRepository;

    @Autowired
    public MovieRatingXrefService(MovieRatingXrefRepository movieRatingXrefRepository) {
        this.movieRatingXrefRepository = movieRatingXrefRepository;
    }

    public void collectRatings(int movieId, MovieModel model) {
        List<MovieRatingXrefEntity> ratings = movieRatingXrefRepository.findAllByMovieId(movieId);
        if (!ratings.isEmpty()) {
            model.setRatingAmount(ratings.size());
            double result = (double) (ratings.stream().mapToInt(MovieRatingXrefEntity::getRating).sum()) / ratings.size();
            model.setRatingValue(result);
        } else {
            model.setRatingAmount(0);
            model.setRatingValue(0);
        }
    }

    public Optional<MovieRatingXrefEntity> findByMovieIdAndUser(int movieId, int userId) {
        return movieRatingXrefRepository.findByMovieIdAndUserId(movieId, userId);
    }

    public void deleteMovieRatingReferenceByMovieId(int movieId) {
        List<MovieRatingXrefEntity> ratings = movieRatingXrefRepository.findAllByMovieId(movieId);
        if (!ratings.isEmpty()) {
            movieRatingXrefRepository.deleteAll(ratings);
        }
    }

    public MovieRatingXrefEntity addRating(MovieEntity movie, UsersEntity user, int rating) {
        MovieRatingXrefEntity movieRatingXrefEntity = new MovieRatingXrefEntity();
        movieRatingXrefEntity.setMovie(movie);
        movieRatingXrefEntity.setUser(user);
        movieRatingXrefEntity.setRating(rating);

        LOGGER.info("addRating: Saving movie rating {} for {}", rating, movie.getName());
        return movieRatingXrefRepository.save(movieRatingXrefEntity);
    }

    public MovieRatingXrefEntity updateRating(MovieRatingXrefEntity movieRating, int rating) {
        movieRating.setRating(rating);
        return movieRatingXrefRepository.save(movieRating);
    }
}
