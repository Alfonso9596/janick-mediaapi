package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.SeriesRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.SeriesModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.SeriesRatingXrefRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeriesRatingXrefService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeriesRatingXrefService.class);

    private final SeriesRatingXrefRepository seriesRatingXrefRepository;

    @Autowired
    public SeriesRatingXrefService(SeriesRatingXrefRepository seriesRatingXrefRepository) {
        this.seriesRatingXrefRepository = seriesRatingXrefRepository;
    }

    public void collectRatings(int seriesId, SeriesModel model) {
        List<SeriesRatingXrefEntity> ratings = seriesRatingXrefRepository.findAllBySeriesId(seriesId);
        if (!ratings.isEmpty()) {
            model.setRatingAmount(ratings.size());
            double result = (double) (ratings.stream().mapToInt(SeriesRatingXrefEntity::getRating).sum()) / ratings.size();
            model.setRatingValue(result);
        } else {
            model.setRatingAmount(0);
            model.setRatingValue(0);
        }
    }

    public Optional<SeriesRatingXrefEntity> findBySeriesIdAndUser(int seriesId, int userId) {
        return seriesRatingXrefRepository.findBySeriesIdAndUserId(seriesId, userId);
    }

    public void deleteSeriesRatingReferenceBySeriesId(int seriesId) {
        List<SeriesRatingXrefEntity> ratings = seriesRatingXrefRepository.findAllBySeriesId(seriesId);
        if (!ratings.isEmpty()) {
            seriesRatingXrefRepository.deleteAll(ratings);
        }
    }

    public SeriesRatingXrefEntity addRating(SeriesEntity series, UsersEntity user, int rating) {
        SeriesRatingXrefEntity seriesRatingXrefEntity = new SeriesRatingXrefEntity();
        seriesRatingXrefEntity.setSeries(series);
        seriesRatingXrefEntity.setUser(user);
        seriesRatingXrefEntity.setRating(rating);

        LOGGER.info("addRating: Saving series rating {} for {}", rating, series.getName());
        return seriesRatingXrefRepository.save(seriesRatingXrefEntity);
    }

    public SeriesRatingXrefEntity updateRating(SeriesRatingXrefEntity seriesRating, int rating) {
        seriesRating.setRating(rating);
        return seriesRatingXrefRepository.save(seriesRating);
    }
}
