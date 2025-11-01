package com.janick_mediadb.janick_mediaapi.entity;

import com.janick_mediadb.janick_mediaapi.entity.xref.SeriesGenreXrefEntity;
import com.janick_mediadb.janick_mediaapi.input.SeriesInput;
import com.janick_mediadb.janick_mediaapi.model.SeriesModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = SeriesEntity.SERIES_TABLE_NAME)
public class SeriesEntity extends AbstractEntity {

    static final String SERIES_TABLE_NAME = "series";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Lob
    @Column(columnDefinition = "text")
    private String description;
    @Column(length = 4)
    private String yearStart;
    @Column(length = 4)
    private String yearEnd;
    private int episodeLength;
    private String posterFilepath;

    @OneToMany(mappedBy = "series")
    private Set<SeriesGenreXrefEntity> seriesGenreXrefs = new HashSet<>();

    public void fromInput(SeriesInput seriesInput) {
        this.name = seriesInput.getName();
        this.description = seriesInput.getDescription();
        this.yearStart = seriesInput.getYearStart();
        this.yearEnd = seriesInput.getYearEnd();
        this.episodeLength = seriesInput.getEpisodeLength();
    }

    public SeriesModel toModel() {
        SeriesModel model = new SeriesModel();
        model.setId(id);
        model.setName(name);
        model.setDescription(description);
        model.setYearStart(yearStart);
        model.setYearEnd(yearEnd);
        model.setEpisodeLength(episodeLength);
        model.setPosterFilepath(posterFilepath);
        return model;
    }
}
