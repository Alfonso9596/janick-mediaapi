package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.SeriesEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SeriesRepository extends JpaRepository<SeriesEntity, Integer>, JpaSpecificationExecutor<SeriesEntity> {

    @Query("SELECT s.name FROM SeriesEntity s")
    List<String> getAllSeriesNames();
}
