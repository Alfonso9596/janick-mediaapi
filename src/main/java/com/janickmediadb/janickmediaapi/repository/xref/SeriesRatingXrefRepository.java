package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.xref.SeriesRatingXrefEntity;
import java.util.LinkedList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRatingXrefRepository extends JpaRepository<SeriesRatingXrefEntity, Integer> {

    LinkedList<SeriesRatingXrefEntity> findAllBySeriesId(int seriesId);

    Optional<SeriesRatingXrefEntity> findBySeriesIdAndUserId(int seriesId, int userId);
}
