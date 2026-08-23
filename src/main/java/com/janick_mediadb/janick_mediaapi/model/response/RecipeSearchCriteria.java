package com.janick_mediadb.janick_mediaapi.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class RecipeSearchCriteria implements Serializable {

    private int id;
    private String name;
    private String mealType;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenfree;
    private Boolean lactosefree;
}
