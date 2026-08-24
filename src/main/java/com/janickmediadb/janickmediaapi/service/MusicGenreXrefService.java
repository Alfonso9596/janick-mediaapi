package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.MusicEntity;
import com.janickmediadb.janickmediaapi.entity.MusicGenreEntity;
import com.janickmediadb.janickmediaapi.entity.xref.MusicGenreXrefEntity;
import com.janickmediadb.janickmediaapi.model.MusicModel;
import com.janickmediadb.janickmediaapi.repository.xref.MusicGenreXrefRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
