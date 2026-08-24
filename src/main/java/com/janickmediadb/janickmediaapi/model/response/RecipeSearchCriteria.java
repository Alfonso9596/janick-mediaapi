package com.janickmediadb.janickmediaapi.model.response;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
