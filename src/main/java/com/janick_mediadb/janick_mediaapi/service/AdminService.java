package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.GameEntity;
import com.janick_mediadb.janick_mediaapi.entity.MovieEntity;
import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.model.GameModel;
import com.janick_mediadb.janick_mediaapi.model.MovieModel;
import com.janick_mediadb.janick_mediaapi.model.SeriesModel;
import com.janick_mediadb.janick_mediaapi.model.response.GameResponse;
import com.janick_mediadb.janick_mediaapi.model.response.MovieResponse;
import com.janick_mediadb.janick_mediaapi.model.response.SeriesResponse;
import com.janick_mediadb.janick_mediaapi.repository.GameRepository;
import com.janick_mediadb.janick_mediaapi.repository.MovieRepository;
import com.janick_mediadb.janick_mediaapi.repository.SeriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MovieGenreXrefService movieGenreXrefService;

    @Autowired
    private MovieRatingXrefService movieRatingXrefService;

    @Autowired
    private SeriesGenreXrefService seriesGenreXrefService;

    @Autowired
    private SeriesRatingXrefService seriesRatingXrefService;

    @Autowired
    private GameGenreXrefService gameGenreXrefService;

    @Autowired
    private GameRatingXrefService gameRatingXrefService;

    public MovieResponse getMoviesData() {
        List<MovieEntity> movies =  movieRepository.findAll();
        List<MovieModel> content = movies.stream().map(m -> {
            MovieModel model = m.toModel();
            movieGenreXrefService.collectGenres(m.getId(), model);
            movieRatingXrefService.collectRatings(m.getId(), model);
            return model;
        }).toList();

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setContent(content);
        movieResponse.setTotalElements(movies.size());

        return movieResponse;
    }

    public SeriesResponse getSeriesData() {
        List<SeriesEntity> series =  seriesRepository.findAll();
        List<SeriesModel> content = series.stream().map(s -> {
            SeriesModel model = s.toModel();
            seriesGenreXrefService.collectGenres(s.getId(), model);
            seriesRatingXrefService.collectRatings(s.getId(), model);
            return model;
        }).toList();

        SeriesResponse seriesResponse = new SeriesResponse();
        seriesResponse.setContent(content);
        seriesResponse.setTotalElements(series.size());

        return seriesResponse;
    }

    public GameResponse getGamesData() {
        List<GameEntity> games =  gameRepository.findAll();
        List<GameModel> content = games.stream().map(g -> {
            GameModel model = g.toModel();
            gameGenreXrefService.collectGenres(g.getId(), model);
            gameRatingXrefService.collectRatings(g.getId(), model);
            return model;
        }).toList();

        GameResponse gameResponse = new GameResponse();
        gameResponse.setContent(content);
        gameResponse.setTotalElements(games.size());

        return gameResponse;
    }
}
