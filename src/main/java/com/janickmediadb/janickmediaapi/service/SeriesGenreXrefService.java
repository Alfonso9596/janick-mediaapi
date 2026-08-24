package com.janickmediadb.janickmediaapi.service;

import com.janickmediadb.janickmediaapi.entity.MovieGenreEntity;
import com.janickmediadb.janickmediaapi.entity.SeriesEntity;
import com.janickmediadb.janickmediaapi.entity.xref.SeriesGenreXrefEntity;
import com.janickmediadb.janickmediaapi.model.SeriesModel;
import com.janickmediadb.janickmediaapi.repository.xref.SeriesGenreXrefRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
