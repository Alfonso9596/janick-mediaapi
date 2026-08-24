package com.janickmediadb.janickmediaapi.repository.xref;

import com.janickmediadb.janickmediaapi.entity.xref.MusicRatingXrefEntity;
import java.util.LinkedList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRatingXrefRepository extends JpaRepository<MusicRatingXrefEntity, Integer> {

    LinkedList<MusicRatingXrefEntity> findAllByMusicId(int musicId);

    Optional<MusicRatingXrefEntity> findByMusicIdAndUserId(int musicId, int userId);
}
