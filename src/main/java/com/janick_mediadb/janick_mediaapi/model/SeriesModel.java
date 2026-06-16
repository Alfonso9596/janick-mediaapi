package com.janick_mediadb.janick_mediaapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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

    private Date createdAt;

    private Date lastUpdated;

    private UserModel user;
}
