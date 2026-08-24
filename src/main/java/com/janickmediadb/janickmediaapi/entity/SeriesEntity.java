package com.janickmediadb.janickmediaapi.entity;

import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import com.janickmediadb.janickmediaapi.entity.xref.SeriesGenreXrefEntity;
import com.janickmediadb.janickmediaapi.input.SeriesInput;
import com.janickmediadb.janickmediaapi.model.SeriesModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

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
    private Instant createdAt;
    private Instant lastUpdated;

    @ManyToOne(targetEntity = UsersEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    @OneToMany(mappedBy = "series")
    private Set<SeriesGenreXrefEntity> seriesGenreXrefs = new HashSet<>();

    public void fromInput(SeriesInput seriesInput) {
        this.name = seriesInput.getName();
        this.description = seriesInput.getDescription();
        this.yearStart = seriesInput.getYearStart();
        this.yearEnd = seriesInput.getYearEnd();
        this.episodeLength = seriesInput.getEpisodeLength();
    }

    public SeriesModel toModel(String timezone) {
        SeriesModel model = new SeriesModel();
        model.setId(id);
        model.setName(name);
        model.setDescription(description);
        model.setYearStart(yearStart);
        model.setYearEnd(yearEnd);
        model.setEpisodeLength(episodeLength);
        model.setPosterFilepath(posterFilepath);
        model.setCreatedAt(createdAt.atZone(ZoneId.of(timezone)));
        model.setLastUpdated(lastUpdated.atZone(ZoneId.of(timezone)));
        model.setUser(user.toModel());
        return model;
    }
}
