package com.janickmediadb.janickmediaapi.model;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeriesModel extends AbstractModel {

    private int id;

    private String name;

    private String description;

    private String yearStart;

    private String yearEnd;

    private int episodeLength;

    private String posterFilepath;

    private double ratingValue;

    private int ratingAmount;

    private List<String> genres;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private UserModel user;
}
