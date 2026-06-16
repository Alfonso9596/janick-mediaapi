package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MovieGenreXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.MovieModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.MovieGenreXrefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieGenreXrefService {

    private final MovieGenreXrefRepository movieGenreXrefRepository;

    @Autowired
    public MovieGenreXrefService(MovieGenreXrefRepository movieGenreXrefRepository) {
        this.movieGenreXrefRepository = movieGenreXrefRepository;
    }

    public List<MovieEntity> findMoviesByGenre(int genreId) {
        return movieGenreXrefRepository.findMoviesByGenre(genreId);
    }

    public void deleteMovieGenreReferenceByMovieId(int movieId) {
        List<MovieGenreXrefEntity> mgs = movieGenreXrefRepository.findMovieGenreReferencesByMovie(movieId);
        if (!mgs.isEmpty()) {
            movieGenreXrefRepository.deleteAll(mgs);
        }
    }

    public void saveMovieGenreXref(MovieEntity movie, List<MovieGenreEntity> genres) {
        if (!genres.isEmpty()) {
            for (MovieGenreEntity genre : genres) {
                MovieGenreXrefEntity mg = new MovieGenreXrefEntity();
                mg.setMovie(movie);
                mg.setGenre(genre);
                movieGenreXrefRepository.save(mg);
            }
        }
    }

    public void collectGenres(int movieId, MovieModel movieModel) {
        List<MovieGenreEntity> genres = movieGenreXrefRepository.findGenresByMovie(movieId);
        movieModel.setGenres(MovieGenreEntity.toModels(genres));
    }
}
