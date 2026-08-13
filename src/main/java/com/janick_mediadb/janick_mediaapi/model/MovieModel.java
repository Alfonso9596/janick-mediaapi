package com.janick_mediadb.janick_mediaapi.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class MovieModel extends AbstractModel {

    private int id;

    private String name;

    private String description;

    private String year;

    private int length;

    private String posterFilepath;

    private double ratingValue;

    private int ratingAmount;

    private List<String> genres;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private UserModel user;
}
