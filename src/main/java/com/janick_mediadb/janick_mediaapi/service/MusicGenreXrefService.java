package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.MusicEntity;
import com.janick_mediadb.janick_mediaapi.entity.MusicGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.MusicGenreXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.MusicModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.MusicGenreXrefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicGenreXrefService {

    private final MusicGenreXrefRepository musicGenreXrefRepository;

    @Autowired
    public MusicGenreXrefService(MusicGenreXrefRepository musicGenreXrefRepository) {
        this.musicGenreXrefRepository = musicGenreXrefRepository;
    }

    public List<MusicEntity> findMusicByGenre(int genreId) {
        return musicGenreXrefRepository.findMusicByGenre(genreId);
    }

    public void deleteMusicGenreReferenceByMusicId(int musicId) {
        List<MusicGenreXrefEntity> mgs = musicGenreXrefRepository.findMusicGenreReferencesByMusic(musicId);
        if (!mgs.isEmpty()) {
            musicGenreXrefRepository.deleteAll(mgs);
        }
    }

    public void saveMusicGenreXref(MusicEntity music, List<MusicGenreEntity> genres) {
        if (!genres.isEmpty()) {
            for (MusicGenreEntity genre : genres) {
                MusicGenreXrefEntity mg = new MusicGenreXrefEntity();
                mg.setMusic(music);
                mg.setGenre(genre);
                musicGenreXrefRepository.save(mg);
            }
        }
    }

    public void collectGenres(int musicId, MusicModel musicModel) {
        List<MusicGenreEntity> genres = musicGenreXrefRepository.findGenresByMusic(musicId);
        musicModel.setGenres(MusicGenreEntity.toModels(genres));
    }
}
