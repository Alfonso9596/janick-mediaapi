package com.janick_mediadb.janick_mediaapi.repository.xref;

import com.janick_mediadb.janick_mediaapi.entity.xref.SeriesRatingXrefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;
import java.util.Optional;

public interface SeriesRatingXrefRepository extends JpaRepository<SeriesRatingXrefEntity, Integer> {

    LinkedList<SeriesRatingXrefEntity> findAllBySeriesId(int seriesId);

    Optional<SeriesRatingXrefEntity> findBySeriesIdAndUserId(int seriesId, int userId);
}
