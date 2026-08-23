package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeriesRepository extends JpaRepository<SeriesEntity, Integer>, JpaSpecificationExecutor<SeriesEntity> {

    @Query("SELECT s.name FROM SeriesEntity s")
    List<String> getAllSeriesNames();
}
