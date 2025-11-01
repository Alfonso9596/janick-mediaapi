package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MovieRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.MovieModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.MovieRatingXrefRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieRatingXrefService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRatingXrefService.class);

    @Autowired
    private MovieRatingXrefRepository movieRatingXrefRepository;

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

    public void deleteMovieRatingReferenceByMovieId(int movieId) {
        List<MovieRatingXrefEntity> ratings = movieRatingXrefRepository.findAllByMovieId(movieId);
        if (!ratings.isEmpty()) {
            movieRatingXrefRepository.deleteAll(ratings);
        }
    }

    public void addRating(MovieEntity movie, int rating) {
        MovieRatingXrefEntity movieRatingXrefEntity = new MovieRatingXrefEntity();
        movieRatingXrefEntity.setMovie(movie);
        movieRatingXrefEntity.setRating(rating);

        LOGGER.info("addRating: Saving movie rating {} for {}", rating, movie.getName());
        movieRatingXrefRepository.save(movieRatingXrefEntity);
    }
}
