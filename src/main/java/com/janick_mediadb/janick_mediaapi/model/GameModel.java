package com.janick_mediadb.janick_mediaapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class GameModel extends AbstractModel {

    private int id;

    private String name;

    private String description;

    private String year;

    private String posterFilepath;

    private double ratingValue;

    private int ratingAmount;

    private List<String> genres;

    private List<String> platforms;

    private Date createdAt;

    private Date lastUpdated;

    private UserModel user;
}
