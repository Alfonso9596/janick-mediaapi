package com.janickmediadb.janickmediaapi.input;

import com.janickmediadb.janickmediaapi.model.AbstractModel;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeInput extends AbstractModel {

    private String name;
    private String description;
    private List<String> mealTypes;
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenfree;
    private boolean lactosefree;
}
