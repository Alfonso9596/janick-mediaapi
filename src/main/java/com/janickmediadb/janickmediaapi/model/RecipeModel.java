package com.janickmediadb.janickmediaapi.model;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeModel extends AbstractModel {

    private int id;

    private String name;

    private String description;

    private String posterFilepath;

    private double ratingValue;

    private int ratingAmount;

    private List<String> mealTypes;

    private boolean vegetarian;

    private boolean vegan;

    private boolean glutenfree;

    private boolean lactosefree;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private UserModel user;
}
