package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.SeriesRatingXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.SeriesModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.SeriesRatingXrefRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesRatingXrefService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeriesRatingXrefService.class);

    @Autowired
    private SeriesRatingXrefRepository seriesRatingXrefRepository;

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

    public void deleteSeriesRatingReferenceBySeriesId(int seriesId) {
        List<SeriesRatingXrefEntity> ratings = seriesRatingXrefRepository.findAllBySeriesId(seriesId);
        if (!ratings.isEmpty()) {
            seriesRatingXrefRepository.deleteAll(ratings);
        }
    }

    public void addRating(SeriesEntity series, int rating) {
        SeriesRatingXrefEntity seriesRatingXrefEntity = new SeriesRatingXrefEntity();
        seriesRatingXrefEntity.setSeries(series);
        seriesRatingXrefEntity.setRating(rating);

        LOGGER.info("addRating: Saving series rating {} for {}", rating, series.getName());
        seriesRatingXrefRepository.save(seriesRatingXrefEntity);
    }
}
