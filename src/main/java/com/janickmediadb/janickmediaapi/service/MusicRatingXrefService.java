package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.MusicEntity;
import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import com.janickmediadb.janickmediaapi.entity.xref.MusicRatingXrefEntity;
import com.janickmediadb.janickmediaapi.model.MusicModel;
import com.janickmediadb.janickmediaapi.repository.xref.MusicRatingXrefRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicRatingXrefService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicRatingXrefService.class);

    private final MusicRatingXrefRepository musicRatingXrefRepository;

    @Autowired
    public MusicRatingXrefService(MusicRatingXrefRepository musicRatingXrefRepository) {
        this.musicRatingXrefRepository = musicRatingXrefRepository;
    }

    public void collectRatings(int musicId, MusicModel model) {
        List<MusicRatingXrefEntity> ratings = musicRatingXrefRepository.findAllByMusicId(musicId);
        if (!ratings.isEmpty()) {
            model.setRatingAmount(ratings.size());
            double result = (double) ratings.stream().mapToInt(MusicRatingXrefEntity::getRating).sum() / ratings.size();
            model.setRatingValue(result);
        } else {
            model.setRatingAmount(0);
            model.setRatingValue(0);
        }
    }

    public Optional<MusicRatingXrefEntity> findByMusicIdAndUser(int musicId, int userId) {
        return musicRatingXrefRepository.findByMusicIdAndUserId(musicId, userId);
    }

    public void deleteMusicRatingReferenceByMusicId(int musicId) {
        List<MusicRatingXrefEntity> ratings = musicRatingXrefRepository.findAllByMusicId(musicId);
        if (!ratings.isEmpty()) {
            musicRatingXrefRepository.deleteAll(ratings);
        }
    }

    public MusicRatingXrefEntity addRating(MusicEntity music, UsersEntity user, int rating) {
        MusicRatingXrefEntity musicRatingXrefEntity = new MusicRatingXrefEntity();
        musicRatingXrefEntity.setMusic(music);
        musicRatingXrefEntity.setUser(user);
        musicRatingXrefEntity.setRating(rating);

        LOGGER.info("addRating: Saving music rating {} for {}", rating, music.getName());
        return musicRatingXrefRepository.save(musicRatingXrefEntity);
    }

    public MusicRatingXrefEntity updateRating(MusicRatingXrefEntity musicRating, int rating) {
        musicRating.setRating(rating);
        return musicRatingXrefRepository.save(musicRating);
    }
}
