package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.MovieEntity;
import com.janickmediadb.janickmediaapi.entity.MovieGenreEntity;
import com.janickmediadb.janickmediaapi.entity.xref.MovieGenreXrefEntity;
import com.janickmediadb.janickmediaapi.model.MovieModel;
import com.janickmediadb.janickmediaapi.repository.xref.MovieGenreXrefRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
