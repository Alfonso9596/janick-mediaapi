package com.janick_mediadb.janick_mediaapi.service;

import com.janick_mediadb.janick_mediaapi.entity.MovieGenreEntity;
import com.janick_mediadb.janick_mediaapi.entity.SeriesEntity;
import com.janick_mediadb.janick_mediaapi.entity.xref.SeriesGenreXrefEntity;
import com.janick_mediadb.janick_mediaapi.model.SeriesModel;
import com.janick_mediadb.janick_mediaapi.repository.xref.SeriesGenreXrefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesGenreXrefService {

    private final SeriesGenreXrefRepository seriesGenreXrefRepository;

    @Autowired
    public SeriesGenreXrefService(SeriesGenreXrefRepository seriesGenreXrefRepository) {
        this.seriesGenreXrefRepository = seriesGenreXrefRepository;
    }

    public List<SeriesEntity> findSeriesByGenre(int genreId) {
        return seriesGenreXrefRepository.findSeriesByGenre(genreId);
    }

    public void deleteSeriesGenreReferenceBySeriesId(int seriesId) {
        List<SeriesGenreXrefEntity> sgs = seriesGenreXrefRepository.findSeriesGenreReferenceBySeries(seriesId);
        if (!sgs.isEmpty()) {
            seriesGenreXrefRepository.deleteAll(sgs);
        }
    }

    public void saveSeriesGenreXref(SeriesEntity series, List<MovieGenreEntity> genres) {
        if (!genres.isEmpty()) {
            for (MovieGenreEntity genre : genres) {
                SeriesGenreXrefEntity sg = new SeriesGenreXrefEntity();
                sg.setSeries(series);
                sg.setGenre(genre);
                seriesGenreXrefRepository.save(sg);
            }
        }
    }

    public void collectGenres(int seriesId, SeriesModel seriesModel) {
        List<MovieGenreEntity> genres = seriesGenreXrefRepository.findGenresBySeries(seriesId);
        seriesModel.setGenres(MovieGenreEntity.toModels(genres));
    }
}
